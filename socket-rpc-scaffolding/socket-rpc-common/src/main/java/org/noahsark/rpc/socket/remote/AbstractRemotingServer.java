package org.noahsark.rpc.socket.remote;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.noahsark.rpc.common.constant.WsConstants;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.socket.event.ServerStartupEvent;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.noahsark.rpc.socket.thread.SessionClearThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public abstract class AbstractRemotingServer implements RemotingServer {

    private static Logger log = LoggerFactory.getLogger(AbstractRemotingServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ChannelInitializer<SocketChannel> serverInitializer;

    private Map<RemoteOption<?>, Object> serverOptions = new HashMap<>();

    private List<ChannelHandler> preHandlers = new ArrayList<>();

    private String dispatcherName = WsConstants.DEFAULT_DISPATCHER_NAME;

    private SessionClearThread sessionClearThread;

    private WorkQueue workQueue;

    private Thread thread;

    private String host;

    private int port;

    private Channel channel;

    private Dispatcher dispatcher;

    /**
     * 初始化
     */
    public void init() {
        if (Epoll.isAvailable()) {
            bossGroup = new EpollEventLoopGroup(1);
            workerGroup = new EpollEventLoopGroup(4); // 先写死，后面优化
        } else {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup(4); // 先写死，后面优化
        }


        initWorkQueue();

        sessionClearThread = new SessionClearThread();
        sessionClearThread.start();

        serverInitializer = getChannelInitializer(this);
    }

    private void initWorkQueue() {

        if (workQueue == null) {
            workQueue = new WorkQueue();
            workQueue.setMaxQueueNum(this.option(RemoteOption.CAPACITY_OF_QUEUE));
            workQueue.setMaxThreadNum(this.option(RemoteOption.THREAD_NUM_OF_QUEUE));

            workQueue.init("Server");
        }

    }

    public AbstractRemotingServer() {
    }

    /**
     * 根据主机及端口构造服务器
     *
     * @param host 主机
     * @param port 端口
     */
    public AbstractRemotingServer(String host, int port) {

        this.host = host;

        this.port = port;

    }

    public AbstractRemotingServer(String host, int port, String dispatcherName) {

        this.host = host;

        this.port = port;

        this.dispatcherName = dispatcherName;

    }

    @Override
    public void start() {

        Runnable runnable = () -> {
            try {

                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(serverInitializer);

                InetSocketAddress address = new InetSocketAddress(host, port);

                ChannelFuture channelFuture = bootstrap.bind(address).sync();
                channelFuture.addListener(getStartListener());

                channel = channelFuture.channel();

                channel.closeFuture().sync();
            } catch (Exception ex) {
                log.warn("Catch an exception.", ex);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        };

        thread = new Thread(runnable, "server-thread");

        thread.start();
    }

    protected abstract ChannelInitializer<SocketChannel> getChannelInitializer(
            AbstractRemotingServer server);

    public <T> void option(RemoteOption<T> option, T value) {
        this.serverOptions.put(option, value);

    }

    public <T> T option(RemoteOption<T> option) {
        return this.serverOptions.containsKey(option) ? (T) this.serverOptions.get(option)
                : option.getDefaultValue();
    }

    public WorkQueue getWorkQueue() {
        return this.workQueue;
    }

    public void setWorkQueue(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }

    public List<ChannelHandler> getPreHandlers() {
        return this.preHandlers;
    }

    public void addPreHandler(ChannelHandler handler) {
        this.preHandlers.add(handler);
    }

    public void registerProcessor(AbstractProcessor<?> processor) {
        processor.register(this.dispatcherName);
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

    @Override
    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        workQueue.shutdown();
        sessionClearThread.shutdown();

        log.info("Shutdown the server...");
    }

    private ChannelFutureListener getStartListener() {
        return future -> {
            if (future.isSuccess()) {
                EventBus.getInstance().post(new ServerStartupEvent(null));
            }
        };
    }

}
