package org.noahsark.rpc.socket.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.noahsark.rpc.common.remote.Subject;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.heartbeat.CommonHeartbeatFactory;
import org.noahsark.rpc.socket.heartbeat.HeartbeatStatus;
import org.noahsark.rpc.socket.session.Session;
import org.noahsark.rpc.socket.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ServerIdleStateTrigger.class);

    private CommonHeartbeatFactory heartbeatFactory = new CommonHeartbeatFactory();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // log.debug("Idle timeout,send heart beat!");

                Session session = Session.getSession(ctx.channel());
                if (session == null) {
                    ctx.disconnect();
                    return;
                }

                Subject subject = session.getSubject();

                if (subject == null) {
                    // 用户为空，直接关闭连接
                    SessionManager.getInstance().disconnect(ctx);

                    return;
                }

                HeartbeatStatus heartbeatStatus = session.getConnectionManager().getHeartbeatStatus();

                if (subject.isRepeatLogin()) {
                    heartbeatStatus.reset();

                    log.info("User repeated login in the same channel:{}", JsonUtils.toJson(subject));

                    return;
                }

                if (heartbeatStatus.incAndTimeout()) {
                    log.info("server time out, disconnect: {}", JsonUtils.toJson(subject));
                    log.info("Session/Channel:{}/{}", session.getSessionId(), ctx.channel());

                    heartbeatStatus.reset();

                    // 连接超过，删除会话
                    SessionManager.getInstance().disconnect(ctx);

                    return;
                }

                String subjectId = null;

                if (subject != null) {
                    subjectId = subject.getId();
                }

                log.info("Idle timeout,send heartbeat:{}/{}", subjectId, heartbeatStatus.getCount());

                session.getConnection().getChannel().writeAndFlush(heartbeatFactory.getPing());

            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
