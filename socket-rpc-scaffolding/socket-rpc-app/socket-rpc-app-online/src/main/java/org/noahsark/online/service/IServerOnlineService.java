package org.noahsark.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.noahsark.online.pojo.po.ServerOnline;

import java.util.Optional;

/**
 * <p>
 * 网关在线 服务类
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
public interface IServerOnlineService extends IService<ServerOnline> {

    Optional<ServerOnline> getSeverOnline(String serverId);

    boolean register(ServerOnline serverInfo);

    void ping(ServerOnline serverInfo);

    boolean isOnline(Optional<ServerOnline> opt);

    void updateStatus(String serverId, Short status);


}