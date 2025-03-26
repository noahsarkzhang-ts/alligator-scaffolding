package org.noahsark.rpc.socket.ws.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.socket.hander.ClientBizServiceHandler;
import org.noahsark.rpc.socket.hander.ClientIdleStateTrigger;
import org.noahsark.rpc.socket.hander.ReconnectHandler;
import org.noahsark.rpc.socket.remote.AbstractRemotingClient;
import org.noahsark.rpc.socket.ws.handler.WebSocketClientDecoder;
import org.noahsark.rpc.socket.ws.handler.WebsocketEncoder;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class WebSocketClientInitializer extends ChannelInitializer<SocketChannel> {

    private final AbstractRemotingClient client;

    private ReconnectHandler reconnectHandler;

    private WorkQueue workQueue;

    private Dispatcher dispatcher;

    /**
     * 构造函数
     *
     * @param client 客户端对象
     */
    public WebSocketClientInitializer(AbstractRemotingClient client) {
        this.client = client;
        this.workQueue = client.getWorkQueue();
        this.dispatcher = client.getDispatcher();

        this.reconnectHandler = new ReconnectHandler(this.client);
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 信任所有证书
        /*SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));*/

        pipeline.addLast(new IdleStateHandler(0, 120, 0));
        pipeline.addLast(new ClientIdleStateTrigger(this.client));
        pipeline.addLast(this.reconnectHandler);
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
        pipeline.addLast(new WebsocketEncoder());
        pipeline.addLast(new WebSocketClientDecoder(
                WebSocketClientHandshakerFactory.newHandshaker(
                        this.client.getServerInfo().getUri(), WebSocketVersion.V13, null,
                        true, new DefaultHttpHeaders()), this.client));
        pipeline.addLast(new ClientBizServiceHandler(this.workQueue, this.dispatcher));

    }

}
