package org.noahsark.rpc.socket.ws.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.noahsark.rpc.common.constant.Cmd;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class WebsocketDecoder extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static Logger log = LoggerFactory.getLogger(WebsocketDecoder.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
        Response response = null;
        String message = null;

        try {
            if (msg instanceof TextWebSocketFrame) {
                // Send the uppercase string back.
                String request = ((TextWebSocketFrame) msg).text();
                log.trace("receive request: {}", request);

                RpcCommand command = RpcCommand.marshalFromJson(request);
                if (command.getType() == RpcCommand.RESPONSE) {
                    command = Response.marshalFromJson(request, command);
                } else {
                    command = Request.build(command);
                }

                if (!executePingPong(ctx, command)) {
                    ctx.fireChannelRead(command);
                }

                return;

                //ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
            } else if (msg instanceof PongWebSocketFrame) {
                // 清空心跳计数
                Session session = Session.getSession(ctx.channel());
                if (session != null) {
                    session.getConnectionManager().getHeartbeatStatus().reset();
                }
                log.trace("WebSocket server received pong");
                return;
            } else {
                message = "unsupported frame type: " + msg.getClass().getName();
                throw new UnsupportedOperationException(message);
            }
        } catch (Exception ex) {
            log.error("catch an exception: ", ex);

            response = new Response.Builder()
                    .cmd(11)
                    .type(RpcCommand.RESPONSE)
                    .seqId(0)
                    .code(Response.FAIL)
                    .msg(message)
                    .build();
        }

        ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(response)));
    }

    private boolean executePingPong(ChannelHandlerContext ctx, RpcCommand command) {
        if (command instanceof Response) {
            Response response = (Response) command;
            if (response.getCmd() == Cmd.CMD_PING_PONG
                    && response.getType() == RpcCommand.RESPONSE) {
                // 清空心跳计数
                Session session = Session.getSession(ctx.channel());
                if (session != null) {
                    session.getConnectionManager().getHeartbeatStatus().reset();
                }
                log.trace("WebSocket server received pong");

                return true;
            }
        }

        return false;
    }
}
