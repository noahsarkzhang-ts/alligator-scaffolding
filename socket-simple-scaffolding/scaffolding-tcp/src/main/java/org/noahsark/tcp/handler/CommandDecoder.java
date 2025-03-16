package org.noahsark.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 解码处理器
 * @author zhangxt
 * @date 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
        throws Exception {

        msg.markReaderIndex();

        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);

        String json = new String(bytes, CharsetUtil.UTF_8);

        msg.markReaderIndex();
        //将处理后的数据，交由handler链中的下一个处理
        out.add(json);

    }
}
