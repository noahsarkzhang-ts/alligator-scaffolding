package org.noahsark.rpc.socket.event;

import org.noahsark.rpc.socket.eventbus.ApplicationEvent;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ClientDisconnectEvent extends ApplicationEvent {

    public ClientDisconnectEvent(Object source) {
        super(source);
    }
}
