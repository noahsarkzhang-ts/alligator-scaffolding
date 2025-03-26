package org.noahsark.gw.ws.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.common.cmd.Cmd;
import org.noahsark.common.constant.ResultConstants;
import org.noahsark.gw.ws.bootstrap.subject.UserSubject;
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
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(AuthHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) {
        // 注册接口放行
        if (msg != null && (msg.getCmd() == Cmd.CMD_DEVICE_LOGIN
                || msg.getCmd() == Cmd.CMD_ADMIN_LOGIN
                || msg.getCmd() == Cmd.CMD_USER_LOGIN)) {

            ctx.fireChannelRead(msg);
            return;
        }

        Session session = Session.getOrCreatedSession(ctx.channel());
        if (session != null) {
            UserSubject subject = (UserSubject) session.getSubject();
            if (subject != null) {
                // 有subject证明已经登录，继续往下走
                ctx.fireChannelRead(msg);
                return;
            }
        }

        // 没有subject是没有登录，直接返回
        log.info("Receive an unauthorized request:{}", JsonUtils.toJson(msg));
        Response response = Response.buildCommonResponse(msg,
                ResultConstants.DEVICE_NOT_LOGIN_CODE,
                ResultConstants.DEVICE_NOT_LOGIN_MSG);
        ctx.channel().writeAndFlush(response);
    }

}
