package org.noahsark.socket.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.noahsark.socket.thread.CommonServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端
 *
 * @author zhangxt
 * @date 2021/3/14
 */
public abstract class AbstractRemotingClient implements RemotingClient {

    private static Logger log = LoggerFactory.getLogger(AbstractRemotingClient.class);

    /**
     * 服务器信息
     */
    protected ServerInfo serverInfo;

    protected Channel channel;

    /**
     * 参数配置
     */
    private Map<RemoteOption<?>, Object> clientOptions = new HashMap<>();


    /**
     * Netty 事件循环对象
     */
    private EventLoopGroup group;

    /**
     * Netty 启动器
     */
    private Bootstrap bootstrap;

    /**
     * 通用任务线程，如重连任务
     */
    private CommonServiceThread commonThread;

    public AbstractRemotingClient() {
    }

    public AbstractRemotingClient(String host, int port) {

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setHost(host);
        serverInfo.setPort(port);

        this.serverInfo = serverInfo;

        init();
    }

    public AbstractRemotingClient(String url) {
        ServerInfo serverInfo = convert(url);

        this.serverInfo = serverInfo;

        init();
    }

    protected void init() {
        try {

            if (!this.existOption(RemoteOption.THREAD_NUM_OF_QUEUE)) {
                this.option(RemoteOption.THREAD_NUM_OF_QUEUE, 5);
            }

            group = new NioEventLoopGroup();

            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(getChannelInitializer(this));

            commonThread = new CommonServiceThread();
            commonThread.start();

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);
        }
    }

    @Override
    public void connect() {

        Runnable runnable = () -> internalConnect();

        commonThread.offer(runnable);

    }

    @Override
    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }

    private void internalConnect() {
        synchronized (bootstrap) {
            ChannelFuture future;

            try {
                future = bootstrap
                        .connect(this.serverInfo.getHost(), this.serverInfo.getPort());
                future.addListener(getConnectionListener());

                // log.info("connect is Done: {}", future.isDone());

                Channel channel = future.sync().channel();
                this.channel = channel;

                log.info("connect is Done: {}", channel);

            } catch (InterruptedException ex) {
                log.warn("catch an exception.", ex);
            }

        }
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
    public void shutdown() {
        group.shutdownGracefully();
    }

    private ChannelFutureListener getConnectionListener() {
        return future -> {

            this.channel = future.channel();

            if (!future.isSuccess()) {
                future.channel().pipeline().fireChannelInactive();
            } else {

                log.info("Connect Completely!!!");
            }
        };
    }

    public abstract ServerInfo convert(String url);

}
