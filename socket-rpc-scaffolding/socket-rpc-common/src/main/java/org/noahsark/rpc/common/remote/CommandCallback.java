package org.noahsark.rpc.common.remote;

/**
 * @author: zhangxt
 * @desc: 回调类
 * @version:
 * @date: 2021/7/21
 */
public interface CommandCallback {

    /**
     * 成功回调函数
     *
     * @param result        结果
     * @param currentFanout 当前结果的计数
     * @param fanout        结果的总数
     */
    void callback(Object result, int currentFanout, int fanout);

    /**
     * 异常回调函数
     *
     * @param cause         异常
     * @param currentFanout 当前结果的计数
     * @param fanout        结果的总数
     */
    void failure(Throwable cause, int currentFanout, int fanout);
}
