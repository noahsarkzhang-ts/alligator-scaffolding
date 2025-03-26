package org.noahsark.rpc.mq.common;

/**
 * 文本消息处理器
 *
 * @author zhangxt
 * @date 2024/03/15 16:00
 **/
public interface StringMessageHandler {
    void onMessage(String msg);
}
