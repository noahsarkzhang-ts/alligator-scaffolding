package org.noahsark.rpc.mq.nats.manager;

import org.noahsark.rpc.mq.common.StringMessageHandler;

import javax.annotation.PostConstruct;

/**
 * MQ消息订阅处理器
 *
 * @author zhangxt
 * @date 2024/03/21 11:24
 **/
public abstract class AbstractMqProcessor implements StringMessageHandler {

    @PostConstruct
    public void register() {
        NatsManager.getInstance().register(getSubject(), this);
    }

    /**
     * 返回订阅的主题
     *
     * @return 请求对应的类的方法
     */
    protected abstract String getSubject();

}
