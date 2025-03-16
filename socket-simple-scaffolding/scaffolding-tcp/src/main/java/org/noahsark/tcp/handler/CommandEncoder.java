package org.noahsark.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 编码处理器
 * @author zhangxt
 * @date 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out)
        throws Exception {

        ByteBuf buf = ctx.alloc().buffer();
        byte [] data = msg.getBytes(CharsetUtil.UTF_8);

        buf.writeBytes(data);

        out.add(buf);
    }
}
