package org.noahsark.rpc.socket.eventbus;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ReconnectEvent extends ApplicationEvent {

    public ReconnectEvent(Object source) {
        super(source);
    }
}
