package org.noahsark.rpc.common.remote;

/**
 * MultiPromise容器
 *
 * @author zhangxt
 * @date 2024/05/15 11:34
 **/
public class MultiPromiseHolder {
    private boolean needResponse;

    private MultiRpcPromise multiPromise;

    public boolean isNeedResponse() {
        return needResponse;
    }

    public void setNeedResponse(boolean needResponse) {
        this.needResponse = needResponse;
    }

    public MultiRpcPromise getMultiPromise() {
        return multiPromise;
    }

    public void setMultiPromise(MultiRpcPromise multiPromise) {
        this.multiPromise = multiPromise;
    }
}
