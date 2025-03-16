package org.noahsark.ws.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import org.noahsark.socket.handler.ServerBizServiceHandler;
import org.noahsark.socket.remote.AbstractRemotingServer;
import org.noahsark.socket.remote.RemoteOption;
import org.noahsark.ws.handler.WebsocketDecoder;
import org.noahsark.ws.handler.WebsocketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebSocket 服务器初始化类
 * @author zhangxt
 * @date 2021/4/3
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger log = LoggerFactory.getLogger(WebSocketServerInitializer.class);

    private static final String WEBSOCKET_PATH = "/websocket";

    private SslContext sslCtx;

    public WebSocketServerInitializer(AbstractRemotingServer server) {
        try {
            if (server.option(RemoteOption.SSL_ENABLE)) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

        } catch (Exception ex) {
            log.info("catch an exception!", ex);
        }
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new IdleStateHandler(300, 0, 0));
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new WebsocketDecoder());
        pipeline.addLast(new WebsocketEncoder());
        pipeline.addLast(new ServerBizServiceHandler());
    }
}
