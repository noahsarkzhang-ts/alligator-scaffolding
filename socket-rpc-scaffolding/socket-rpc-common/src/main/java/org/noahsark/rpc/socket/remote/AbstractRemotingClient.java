package org.noahsark.rpc.socket.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.noahsark.rpc.common.constant.WsConstants;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.DispatcherFactory;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.remote.*;
import org.noahsark.rpc.socket.event.ClientConnectionSuccessEvent;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.noahsark.rpc.socket.session.Connection;
import org.noahsark.rpc.socket.session.ConnectionManager;
import org.noahsark.rpc.socket.thread.ClientClearThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public abstract class AbstractRemotingClient implements RemotingClient {

    private static Logger log = LoggerFactory.getLogger(AbstractRemotingClient.class);

    protected ServerInfo current;

    private EventLoopGroup group;

    private Map<RemoteOption<?>, Object> clientOptions = new HashMap<>();

    private WorkQueue workQueue;

    private Dispatcher dispatcher;

    protected ConnectionManager connectionManager;

    protected ServerManager serverManager;

    protected Connection connection;

    private Bootstrap bootstrap;

    private ClientClearThread clearThread;

    private Thread thread;

    public AbstractRemotingClient() {
    }

    /**
     * 构造客户端
     *
     * @param url 一个地址
     */
    public AbstractRemotingClient(String url) {

        ServerInfo serverInfo = convert(url);

        List<ServerInfo> servers = new ArrayList<>();
        servers.add(serverInfo);

        serverManager = new ServerManager(servers);

        this.current = serverManager.toggleServer();

        init();
    }

    /**
     * 构造客户端
     *
     * @param urls 多个地址
     */
    public AbstractRemotingClient(List<String> urls) {
        List<ServerInfo> servers = new ArrayList<>();

        urls.stream().forEach(url -> servers.add(this.convert(url)));

        serverManager = new ServerManager(servers);
        this.current = serverManager.toggleServer();

        init();
    }

    /**
     * 根据主机及端口构造客户端
     *
     * @param host 主机
     * @param port 端口
     */
    public AbstractRemotingClient(String host, int port) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(host + ":" + port);
        serverInfo.setHost(host);
        serverInfo.setPort(port);

        List<ServerInfo> servers = new ArrayList<>();
        servers.add(serverInfo);

        serverManager = new ServerManager(servers);

        this.current = serverManager.toggleServer();

        init();
    }

    protected void init() {
        try {
            preInit();

            if (!this.existOption(RemoteOption.THREAD_NUM_OF_QUEUE)) {
                this.option(RemoteOption.THREAD_NUM_OF_QUEUE, 5);
            }

            initWorkQueue();

            dispatcher = DispatcherFactory.getDispatcher(WsConstants.DEFAULT_DISPATCHER_NAME);

            connection = new Connection();

            clearThread = new ClientClearThread();
            clearThread.start();

            group = new NioEventLoopGroup();

            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(getChannelInitializer(this));

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);
        }
    }

    private void initWorkQueue() {
        this.workQueue = new WorkQueue();
        this.workQueue.setMaxQueueNum(this.option(RemoteOption.CAPACITY_OF_QUEUE));
        this.workQueue.setMaxThreadNum(this.option(RemoteOption.THREAD_NUM_OF_QUEUE));

        this.workQueue.init("Client");
    }

    protected abstract ChannelInitializer<SocketChannel> getChannelInitializer(
            AbstractRemotingClient server);

    public <T> void option(RemoteOption<T> option, T value) {
        this.clientOptions.put(option, value);

    }

    public <T> T option(RemoteOption<T> option) {
        return this.clientOptions.containsKey(option) ? (T) this.clientOptions.get(option)
                : option.getDefaultValue();
    }

    public <T> boolean existOption(RemoteOption<T> option) {
        return this.clientOptions.containsKey(option);
    }

    @Override
    public void connect() {

        Runnable runnable = () -> {
            synchronized (bootstrap) {
                ChannelFuture future = bootstrap
                        .connect(current.getHost(), current.getPort());
                future.addListener(getConnectionListener());
                Channel channel = future.channel();
                connection.setChannel(channel);

                future.awaitUninterruptibly();
            }
        };

        thread = new Thread(runnable, "client-thread-");
        thread.start();
    }

    @Override
    public void shutdown() {
        if (this.connection != null) {
            connection.close();
        }
        group.shutdownGracefully();

        if (clearThread != null) {
            clearThread.shutdown();
        }

    }

    @Override
    public RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();
        request.setSeqId(this.connection.nextId());

        promise.invoke(this.connection, request, commandCallback, timeoutMillis);

        return promise;
    }

    /**
     * 同步调用
     *
     * @param request       请求
     * @param timeoutMillis 超时时间
     * @return 结果
     */
    public Object invokeSync(Request request, int timeoutMillis) {
        RpcPromise promise = new RpcPromise();
        request.setSeqId(this.connection.nextId());

        Object result = promise.invokeSync(this.connection, request, timeoutMillis);

        return result;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    @Override
    public void toggleServer() {
        ServerInfo serverInfo = serverManager.toggleServer();

        if (serverInfo != null) {
            this.current = serverInfo;

            log.info("toggle to new server: {} : {}",
                    serverInfo.getHost(), serverInfo.getPort());

            this.connect();
        } else {

            log.info("No server to toggle,reset servers.");
            serverManager.reset();

            final EventLoop eventLoop = this.connection.getChannel().eventLoop();

            eventLoop.schedule(() -> {
                log.info("Try Reconnecting ...");

                this.toggleServer();
            }, 3000, TimeUnit.MILLISECONDS);
        }

    }

    public WorkQueue getWorkQueue() {
        return this.workQueue;
    }

    @Override
    public ServerInfo getServerInfo() {
        return this.current;
    }

    protected abstract void preInit();

    public abstract ServerInfo convert(String url);

    public void registerProcessor(AbstractProcessor<?> processor) {
        processor.register();
    }

    public void unregisterProcessor(AbstractProcessor<?> processor) {
        processor.unregister();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    private ChannelFutureListener getConnectionListener() {
        return future -> {
            if (!future.isSuccess()) {
                future.channel().pipeline().fireChannelInactive();
            } else {
                EventBus.getInstance().post(new ClientConnectionSuccessEvent(null));
            }
        };
    }


}
