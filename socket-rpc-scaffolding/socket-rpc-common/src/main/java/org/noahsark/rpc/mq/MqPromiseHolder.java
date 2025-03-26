package org.noahsark.rpc.mq;

import org.noahsark.rpc.common.exception.InvokeExcption;
import org.noahsark.rpc.common.remote.PromiseHolder;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.RpcPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MQ promise 管理
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public class MqPromiseHolder implements PromiseHolder {

    private static Logger logger = LoggerFactory.getLogger(MqPromiseHolder.class);

    private AtomicInteger nextId = new AtomicInteger(1);

    private Producer producer;

    private final ConcurrentHashMap<Integer, RpcPromise> futures = new ConcurrentHashMap<>(16);

    public MqPromiseHolder() {
    }

    public MqPromiseHolder(Producer producer) {
        this.producer = producer;
    }

    @Override
    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    @Override
    public void registerPromise(Integer requestId, RpcPromise promise) {
        futures.put(requestId, promise);
    }

    @Override
    public RpcPromise removePromise(Integer requestId) {

        RpcPromise promise = futures.get(requestId);

        if (promise.isRemoving()) {
            this.futures.remove(requestId);
        }

        return promise;
    }

    @Override
    public void removePromise(RpcPromise promise) {

        if (promise.isRemoving()) {
            this.futures.remove(promise.getRequestId());
        }

    }

    @Override
    public int nextId() {
        return nextId.getAndIncrement();
    }

    @Override
    public void write(RpcCommand command) {

        Message msg = producer.buildMessage(command);

        producer.send(msg, new SendCallback() {

            @Override
            public void onSuccess(SendResult var1) {
                logger.info("send command successfully:{}", command.getSeqId());
            }

            @Override
            public void onException(Throwable var1) {
                logger.error("send command fail!", var1);

                RpcPromise promise = MqPromiseHolder.this
                        .removePromise(command.getSeqId());
                if (promise != null) {

                    promise.setFailure(new InvokeExcption());
                }

            }
        }, 3000);
    }

}
