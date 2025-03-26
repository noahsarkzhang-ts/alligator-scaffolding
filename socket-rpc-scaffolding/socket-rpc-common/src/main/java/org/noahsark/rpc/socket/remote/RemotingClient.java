package org.noahsark.rpc.socket.remote;

import org.noahsark.rpc.common.remote.CommandCallback;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.RpcPromise;
import org.noahsark.rpc.socket.session.ConnectionManager;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public interface RemotingClient {

    void connect();

    void shutdown();

    void ping();

    void toggleServer();

    ServerInfo getServerInfo();

    ConnectionManager getConnectionManager();

    void sendMessage(RpcCommand command);

    RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis);

}
