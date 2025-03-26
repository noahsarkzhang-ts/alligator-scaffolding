package org.noahsark.gw.ws.bootstrap.busevent;

import org.noahsark.rpc.socket.event.ServerStartupEvent;
import org.noahsark.rpc.socket.eventbus.ApplicationListener;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: noahsark
 * @version: 服务器启动成功
 * @date: 2021/4/12
 */
@Component
public class ServerStartupListener extends ApplicationListener<ServerStartupEvent> {

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }
}
