package org.noahsark.rpc.socket.ws.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.JsonUtils;

import java.util.List;

/**
 * Created by hadoop on 2021/4/3.
 */
public class WebsocketEncoder extends MessageToMessageEncoder<RpcCommand> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcCommand msg, List<Object> out)
            throws Exception {

        //        Object payload = msg.getData();
        //        if (payload instanceof byte[]) {
        //            String srcPayload = new String((byte[]) payload);
        //            JsonObject targetPaylpad = new JsonParser().parse(srcPayload).getAsJsonObject();
        //
        //            msg.setData(targetPaylpad);
        //        }

        out.add(new TextWebSocketFrame(JsonUtils.toJson(msg)));
    }
}
