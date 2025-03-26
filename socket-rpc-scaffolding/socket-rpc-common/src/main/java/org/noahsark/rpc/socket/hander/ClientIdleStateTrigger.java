package org.noahsark.rpc.socket.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.noahsark.rpc.socket.remote.AbstractRemotingClient;
import org.noahsark.rpc.socket.remote.RemotingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc: 用于捕获dleState事件（未在指定时间内向服务器发送数据），然后向Server端发送一个心跳包
 * @version:
 * @date: 2021/7/21
 */
public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ClientIdleStateTrigger.class);

    private RemotingClient remotingClient;

    public ClientIdleStateTrigger(AbstractRemotingClient remotingClient) {
        this.remotingClient = remotingClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {

                log.info("client timeout, and toggle server");

                this.remotingClient.toggleServer();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}
