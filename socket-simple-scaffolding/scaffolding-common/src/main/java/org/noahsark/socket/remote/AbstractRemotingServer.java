package org.noahsark.socket.remote;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器
 * @author zhangxt
 * @date  2021/3/14
 */
public abstract class AbstractRemotingServer implements RemotingServer {

    private static Logger log = LoggerFactory.getLogger(AbstractRemotingServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ChannelInitializer<SocketChannel> serverInitializer;

    private Map<RemoteOption<?>, Object> serverOptions = new HashMap<>();

    private Thread thread;

    private String host;

    private int port;

    private Channel channel;

    public void init() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        serverInitializer = getChannelInitializer(this);
    }


    public AbstractRemotingServer() {
    }

    public AbstractRemotingServer(String host, int port) {
        this.host = host;
        this.port = port;

    }

    @Override
    public void start() {

        Runnable runnable = () -> {
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(serverInitializer);

                InetSocketAddress address = new InetSocketAddress(host, port);

                ChannelFuture channelFuture = bootstrap.bind(address);
                channelFuture.addListener(getStartListener());
                channelFuture.sync();

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

    @Override
    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        log.info("Shutdown the server...");
    }

    private ChannelFutureListener getStartListener() {
        return future -> {
            if (future.isSuccess()) {
                log.info("Server start successfully!");
            }
        };
    }

}
