package org.noahsark.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端通用处理类
 * @author zhangxt
 * @date 2021/4/3
 */
public class ClientBizServiceHandler extends SimpleChannelInboundHandler<String> {

    private static Logger log = LoggerFactory.getLogger(ClientBizServiceHandler.class);

    public ClientBizServiceHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        log.info("receive msg: {}", msg);

        try {

        } catch (Exception ex) {
            log.warn("catch an exception:{}", ex);
        }
    }
}
