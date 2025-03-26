package org.noahsark.rpc.socket.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class AuthHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(AuthHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) {
        // 注册接口放行
        /*if (msg != null && msg.getSeqId() == 1001) {
            // 首位用户登录情况，校验主备情况，非主节点直接断开连接
            if (!mainNodeStatus(ctx)) {
                return;
            }
            ctx.fireChannelRead(msg);
            return;
        }
        Session session = Session.getOrCreatedSession(ctx.channel());
        CloudSubject subject = (CloudSubject) session.getSubject();
        if (!ObjectUtils.isEmpty(subject)) {
            // 有subject证明已经登录，继续往下走
            ctx.fireChannelRead(msg);
            return;
        }
        // 没有subject是没有登录，直接返回
        Response response = Response.defaultResponse(msg);
        response.setCode(Contants.NO_LOGIN_CODE);
        response.setMsg(Contants.NO_LOGIN_MSG);
        ctx.channel().writeAndFlush(response);*/
        ctx.fireChannelRead(msg);
    }

}
