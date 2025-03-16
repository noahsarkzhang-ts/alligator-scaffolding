package org.noahsark.ws.handler;

import com.google.gson.JsonSyntaxException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端解码处理器
 *
 * @author zhangxt
 * @date 2021/3/28
 */
public class WebsocketDecoder extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static Logger log = LoggerFactory.getLogger(WebsocketDecoder.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {

        try {

            if (msg instanceof TextWebSocketFrame) {
                // Send the uppercase string back.
                String request = ((TextWebSocketFrame) msg).text();
                log.info("receive request: {}", request);

                ctx.fireChannelRead(request);

            } else {
                String message = "unsupported frame type: " + msg.getClass().getName();
                throw new UnsupportedOperationException(message);
            }
        } catch (JsonSyntaxException ex) {
            log.error("catch an exception: ", ex);

        } catch (Exception ex) {
            log.error("catch an exception: ", ex);
        }

    }
}
