package org.noahsark.ws.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import org.noahsark.socket.handler.ClientBizServiceHandler;
import org.noahsark.socket.remote.AbstractRemotingClient;
import org.noahsark.ws.handler.WebSocketClientDecoder;
import org.noahsark.ws.handler.WebsocketEncoder;

/**
 * Websocket 客户端初始化类
 *
 * @author zhangxt
 * @date 2021/3/7.
 */
public class WebSocketClientInitializer extends ChannelInitializer<SocketChannel> {

    private final AbstractRemotingClient client;

    public WebSocketClientInitializer(AbstractRemotingClient client) {
        this.client = client;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
        pipeline.addLast(new WebsocketEncoder());
        pipeline.addLast(new WebSocketClientDecoder(
                WebSocketClientHandshakerFactory.newHandshaker(
                        this.client.getServerInfo().getUri(), WebSocketVersion.V13, null,
                        true, new DefaultHttpHeaders())));
        pipeline.addLast(new ClientBizServiceHandler());

    }

}
