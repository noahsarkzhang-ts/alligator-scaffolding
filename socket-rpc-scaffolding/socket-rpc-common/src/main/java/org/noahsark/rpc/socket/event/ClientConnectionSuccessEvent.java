package org.noahsark.rpc.socket.event;

import org.noahsark.rpc.socket.eventbus.ApplicationEvent;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ClientConnectionSuccessEvent extends ApplicationEvent {

    public ClientConnectionSuccessEvent(Object source) {
        super(source);
    }
}
