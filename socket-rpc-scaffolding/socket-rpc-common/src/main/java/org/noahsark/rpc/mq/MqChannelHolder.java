package org.noahsark.rpc.mq;

import org.noahsark.rpc.common.remote.ChannelHolder;
import org.noahsark.rpc.common.remote.PromiseHolder;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.Subject;

/**
 * MQ 通道管理
 * @author zhangxt
 * @date 2021/4/29
 */
public class MqChannelHolder implements ChannelHolder {

    private Producer producer;

    private PromiseHolder promiseHolder;

    public MqChannelHolder() {
    }

    public MqChannelHolder(Producer producer, PromiseHolder promiseHolder) {
        this.producer = producer;
        this.promiseHolder = promiseHolder;
    }

    @Override
    public void write(RpcCommand response) {

        this.promiseHolder.write(response);

    }

    @Override
    public PromiseHolder getPromiseHolder() {
        return this.promiseHolder;
    }

    @Override
    public Subject getSubject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubject(Subject subject) {
        throw new UnsupportedOperationException();
    }
}
