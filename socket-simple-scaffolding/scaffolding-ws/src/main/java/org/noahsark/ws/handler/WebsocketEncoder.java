package org.noahsark.ws.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * 编码处理器
 * @author zhangxt
 * @date 2021/4/3
 */
public class WebsocketEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out)
            throws Exception {

        out.add(new TextWebSocketFrame(msg));
    }
}
