package org.noahsark.rpc.socket.eventbus;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public abstract class ApplicationListener<E extends ApplicationEvent> implements EventListener {

    public abstract void onApplicationEvent(E event);

}
