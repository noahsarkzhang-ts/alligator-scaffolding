package org.noahsark.rpc.socket.remote;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public interface RemotingServer {

    /**
     * 启动远程服务
     */
    void start();

    /**
     * 关闭远程服务
     */
    void shutdown();

}
