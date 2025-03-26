package org.noahsark.rpc.common.remote;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public interface ChannelHolder {

    /**
     *  向通道中写入数据
     * @param response 响应结果
     */
    void write(RpcCommand response);

    /**
     * 获取promisHolder结构
     * @return PromisHolder
     */
    PromiseHolder getPromiseHolder();

    /**
     *  获取subject
     * @return Subject
     */
    Subject getSubject();

    /**
     *  设置subject
     */
    void setSubject(Subject subject);

}

