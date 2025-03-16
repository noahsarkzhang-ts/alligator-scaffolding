package org.noahsark.socket.processor;

import io.netty.channel.ChannelHandlerContext;

/**
 * 通用消息处理器
 * @author zhangxt
 * @date 2020/12/3
 */
public interface IProcessor{
    void execute(ChannelHandlerContext ctx, String command);

}

