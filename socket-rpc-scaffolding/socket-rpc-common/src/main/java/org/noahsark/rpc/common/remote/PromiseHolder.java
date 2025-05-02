package org.noahsark.rpc.common.remote;

/**
 * @author: zhangxt
 * @desc: promise容器类
 * @version:
 * @date: 2021/7/21
 */
public interface PromiseHolder {

    void removePromise(RpcPromise promise);

    RpcPromise removePromise(Integer requestId);

    void registerPromise(Integer requestId, RpcPromise promise);

    RpcPromise getPromise(Integer requestId);

    void write(RpcCommand command);

    int nextId();
}
