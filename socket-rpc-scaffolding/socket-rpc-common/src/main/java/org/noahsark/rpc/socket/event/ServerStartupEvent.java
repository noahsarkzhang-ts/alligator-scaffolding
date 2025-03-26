package org.noahsark.rpc.socket.event;


import org.noahsark.rpc.socket.eventbus.ApplicationEvent;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ServerStartupEvent extends ApplicationEvent {

    public ServerStartupEvent(Object source) {
        super(source);
    }
}
