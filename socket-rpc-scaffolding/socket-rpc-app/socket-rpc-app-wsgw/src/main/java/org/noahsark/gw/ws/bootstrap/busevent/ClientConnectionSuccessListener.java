package org.noahsark.gw.ws.bootstrap.busevent;

import org.noahsark.rpc.socket.event.ClientConnectionSuccessEvent;
import org.noahsark.rpc.socket.eventbus.ApplicationListener;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: zhangxt
 * @desc: 客户端上线监听器
 * @version:
 * @date: 2021/7/21
 */
@Component
public class ClientConnectionSuccessListener extends ApplicationListener<ClientConnectionSuccessEvent> {

    @Override
    public void onApplicationEvent(ClientConnectionSuccessEvent event) {

    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }
}
