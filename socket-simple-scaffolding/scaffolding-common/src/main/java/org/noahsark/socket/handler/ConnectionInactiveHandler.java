package org.noahsark.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接断开之后的处理器
 * @author zhangxt
 * @date 2021/6/30
 */
public class ConnectionInactiveHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ConnectionInactiveHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        log.info("client inactive!!!");

        // 连接超过，删除会话

    }
}
