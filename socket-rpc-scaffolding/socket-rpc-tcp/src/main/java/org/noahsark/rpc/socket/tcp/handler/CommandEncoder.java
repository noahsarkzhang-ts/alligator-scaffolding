package org.noahsark.rpc.socket.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.JsonUtils;

import java.util.List;

/**
 * 编码处理器
 *
 * @author zhangxt
 * @date 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandEncoder extends MessageToMessageEncoder<RpcCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcCommand msg, List<Object> out)
            throws Exception {

        ByteBuf buf = ctx.alloc().buffer();
        byte[] data = JsonUtils.toJson(msg).getBytes(CharsetUtil.UTF_8);

        buf.writeBytes(data);

        out.add(buf);

    }
}
