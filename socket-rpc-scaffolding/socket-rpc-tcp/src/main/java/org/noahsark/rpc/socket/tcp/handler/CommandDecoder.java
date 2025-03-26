package org.noahsark.rpc.socket.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;

import java.util.List;

/**
 * 解码处理器
 *
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

        RpcCommand command = RpcCommand.marshalFromJson(json);
        if (command.getType() == RpcCommand.RESPONSE) {
            command = Response.marshalFromJson(json, command);
        } else {
            command = Request.build(command);
        }

        //将处理后的数据，交由handler链中的下一个处理
        out.add(command);

    }
}
