package org.noahsark.rpc.mq;

import org.noahsark.rpc.common.remote.ChannelHolder;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.PromiseHolder;
import org.noahsark.rpc.common.remote.RpcPromise;

/**
 * MQ代理抽象类
 *
 * @author zhangxt
 * @date 22021/5/3
 */
public abstract class AbstractMqProxy implements MqProxy {

    protected PromiseHolder promiseHolder;

    protected ChannelHolder channelHolder;

    protected abstract void initHolder();

    @Override
    public RpcPromise sendAsync(MqMultiRequest request,
                                CommandCallback commandCallback,
                                int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        promise.invoke(this.promiseHolder, request, commandCallback, timeoutMillis);

        return promise;
    }

    @Override
    public Object sendSync(MqMultiRequest request, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        Object result = promise.invokeSync(this.promiseHolder, request, timeoutMillis);

        return result;
    }

    @Override
    public PromiseHolder getPromiseHolder() {
        return this.promiseHolder;
    }

    @Override
    public ChannelHolder getChannelHolder() {
        return this.channelHolder;
    }
}
