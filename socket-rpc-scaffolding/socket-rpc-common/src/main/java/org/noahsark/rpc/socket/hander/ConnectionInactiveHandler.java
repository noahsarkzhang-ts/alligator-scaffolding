package org.noahsark.rpc.socket.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.noahsark.rpc.socket.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ConnectionInactiveHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ConnectionInactiveHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {


        try {
            log.info("client inactive:{}", ctx.channel());

            // 连接超过，删除会话
            SessionManager.getInstance().disconnect(ctx);
        } catch (Exception ex) {
            log.warn("Catch an exception when disconnecting channel.", ex);
        }
    }
}
