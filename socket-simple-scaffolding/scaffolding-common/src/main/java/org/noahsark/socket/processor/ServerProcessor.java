package org.noahsark.socket.processor;

import io.netty.channel.ChannelHandlerContext;
import org.noahsark.socket.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 服务端处理器
 *
 * @author zhangxt
 * @date 2025/03/16 11:54
 **/
public class ServerProcessor implements IProcessor {

    private static Logger log = LoggerFactory.getLogger(ServerProcessor.class);

    @Override
    public void execute(ChannelHandlerContext ctx, String command) {

        log.info("receive device met data:{}", command);

    }
}
