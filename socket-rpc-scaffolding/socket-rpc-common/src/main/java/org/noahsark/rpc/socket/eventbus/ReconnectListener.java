package org.noahsark.rpc.socket.eventbus;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ReconnectListener extends ApplicationListener<ReconnectEvent> {

    @Override
    public void onApplicationEvent(ReconnectEvent event) {
        System.out.println("timestamp = " + event.getTimestamp());

    }
}
