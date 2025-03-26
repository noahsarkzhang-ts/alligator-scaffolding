package org.noahsark.rpc.socket.tcp.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.rpc.common.constant.Cmd;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.socket.remote.RemotingClient;
import org.noahsark.rpc.socket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pong 处理器
 *
 * @author zhangxt
 * @date 2021/4/3
 */
@ChannelHandler.Sharable
public class PongHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(PongHandler.class);

    private RemotingClient client;

    public PongHandler(RemotingClient client) {
        this.client = client;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand command) throws Exception {

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

                return;
            }
        }

        ctx.fireChannelRead(command);

    }
}
