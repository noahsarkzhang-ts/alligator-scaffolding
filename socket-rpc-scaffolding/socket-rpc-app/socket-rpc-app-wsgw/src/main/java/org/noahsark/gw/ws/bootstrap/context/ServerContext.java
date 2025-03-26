package org.noahsark.gw.ws.bootstrap.context;

import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.socket.remote.AbstractRemotingServer;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public final class ServerContext {

    public static AbstractRemotingServer server;

    public static Dispatcher dispatcher;
}
