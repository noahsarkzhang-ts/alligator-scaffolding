package org.noahsark.rpc.socket.remote;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ServerManager {

    private ServerInfo current;

    private List<ServerInfo> all;

    private List<ServerInfo> avialable;

    public ServerManager() {
        all = new ArrayList<>();
        avialable = new ArrayList<>();
    }

    /**
     *  构造函数
     * @param servers 多个服务器
     */
    public ServerManager(List<ServerInfo> servers) {
        this();

        all.addAll(servers);
        avialable.addAll(servers);
    }

    /**
     *  切换服务器
     * @return 合适的服务器
     */
    public ServerInfo toggleServer() {

        ServerInfo serverInfo = null;

        if (!avialable.isEmpty()) {
            serverInfo = avialable.get(0);

            current = serverInfo;

            avialable.remove(serverInfo);

            return serverInfo;
        }

        return serverInfo;
    }

    public void reset() {
        avialable.addAll(all);
    }
}
