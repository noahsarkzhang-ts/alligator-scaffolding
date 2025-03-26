package org.noahsark.rpc.mq;

import org.noahsark.rpc.common.remote.ChannelHolder;
import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.PromiseHolder;
import org.noahsark.rpc.common.remote.RpcPromise;

/**
 * MQ 代理接口
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public interface MqProxy {

    PromiseHolder getPromiseHolder();

    ChannelHolder getChannelHolder();

    RpcPromise sendAsync(MqMultiRequest request, CommandCallback commandCallback, int timeoutMillis);

    Object sendSync(MqMultiRequest request, int timeoutMillis);

    void sendOneway(MqMultiRequest request);

    void start();

    void shutdown();
}
