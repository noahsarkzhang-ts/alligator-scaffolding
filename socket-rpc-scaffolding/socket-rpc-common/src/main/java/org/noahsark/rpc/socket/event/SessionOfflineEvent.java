package org.noahsark.rpc.socket.event;

import org.noahsark.rpc.socket.eventbus.ApplicationEvent;

/**
 * @author: zhangxt
 * @desc: 会话下线事件
 * @version:
 * @date: 2021/7/21
 */
public class SessionOfflineEvent extends ApplicationEvent {

    public SessionOfflineEvent(Object source) {
        super(source);
    }
}
