package org.noahsark.socket.remote;

/**
 * 客户端接口
 *
 * @author zhangxt
 * @date 2021/03/07
 */
public interface RemotingClient {

    /**
     * 连接服务器
     */
    void connect();

    /**
     * 发送数据（响应）
     *
     * @param command 响应
     */
    void sendMessage(String command);

    /**
     * 获取服务器信息
     *
     * @return 服务器信息
     */
    ServerInfo getServerInfo();

    /**
     * 连接服务器
     */

    /**
     * 关闭连接
     */
    void shutdown();


}
