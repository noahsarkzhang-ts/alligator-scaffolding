package org.noahsark.rpc.socket.ws.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.socket.heartbeat.ExponentialBackOffRetry;
import org.noahsark.rpc.socket.heartbeat.PingProcessor;
import org.noahsark.rpc.socket.remote.AbstractRemotingClient;
import org.noahsark.rpc.socket.remote.ServerInfo;
import org.noahsark.rpc.socket.session.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public final class WebSocketClient extends AbstractRemotingClient {

    private static Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    public WebSocketClient() {
    }

    public WebSocketClient(String url) {
        super(url);
        registerDefaultProcessor();
    }

    public WebSocketClient(List<String> urls) {
        super(urls);
        registerDefaultProcessor();

    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(
        AbstractRemotingClient server) {
        return new WebSocketClientInitializer(this);
    }

    public void sendMessage(WebSocketFrame frame) {
        this.connection.getChannel().writeAndFlush(frame);

    }

    @Override
    public void sendMessage(RpcCommand command) {

        this.connection.getChannel().writeAndFlush(command);
    }

    @Override
    protected void preInit() {

        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.setHeartbeatFactory(new WebsocketHeartbeatFactory());
        connectionManager.setRetryPolicy(new ExponentialBackOffRetry(1000,
            4, 60 * 1000));

        this.connectionManager = connectionManager;

    }

    @Override
    public void ping() {
        this.sendMessage((WebSocketFrame) this.connectionManager.getHeartbeatFactory().getPing());
    }

    @Override
    public ServerInfo convert(String url) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(url);

        try {

            URI uri = new URI(url);

            String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            int port = uri.getPort();

            serverInfo.setHost(host);
            serverInfo.setPort(port);
            serverInfo.setUri(uri);

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);

        }

        return serverInfo;
    }

    private void registerDefaultProcessor() {
        this.registerProcessor(new PingProcessor());
    }

    public static void main(String[] args) throws Exception {

    }
}
