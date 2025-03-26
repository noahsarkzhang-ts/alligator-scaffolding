package org.noahsark.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.online.mapper.ServerOnlineMapper;
import org.noahsark.online.pojo.po.ServerOnline;
import org.noahsark.online.service.IServerOnlineService;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.util.CollectionsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * <p>
 * 网关在线 服务实现类
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
@Service
public class ServerOnlineServiceImpl extends ServiceImpl<ServerOnlineMapper, ServerOnline>
        implements IServerOnlineService {

    private static Logger log = LoggerFactory.getLogger(ServerOnlineServiceImpl.class);

    /**
     * 服务超时时间，5分钟
     */
    private static final int SERVER_TIMEOUT_SECOND = 5 * 60;

    @Autowired
    private ISubjectOnlineService subjectOnlineService;

    public Optional<ServerOnline> getSeverOnline(String serverId) {
        QueryWrapper<ServerOnline> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_id", serverId);

        List<ServerOnline> list = this.list(queryWrapper);

        if (!CollectionsUtils.isEmpty(list)) {
            return Optional.of(list.get(0));
        } else {
            return Optional.ofNullable(null);
        }

    }

    public boolean register(ServerOnline serverInfo) {

        if (serverInfo == null) {
            return false;
        }

        String serverId = serverInfo.getServerId();

        Optional<ServerOnline> opt = getSeverOnline(serverId);

        if (opt.isEmpty()) {
            // 注册
            serverInfo.setId(IdWorker.getId(serverInfo));
            serverInfo.setStatus((short) 1);
            serverInfo.setLoginTime(LocalDateTime.now());
            serverInfo.setLastPingTime(LocalDateTime.now());

            this.save(serverInfo);

        } else {
            ping(opt.get());
        }

        // 清除之前的服务状态
        clearServer(serverId);

        return true;

    }

    public void ping(ServerOnline serverInfo) {

        if (serverInfo == null) {
            return;
        }

        ServerOnline updateServer = new ServerOnline();
        updateServer.setLastPingTime(LocalDateTime.now());
        updateServer.setStatus((short) 1);

        String serverId = serverInfo.getServerId();
        UpdateWrapper<ServerOnline> updateWr = new UpdateWrapper<>();
        updateWr.eq("server_id", serverId);

        this.update(updateServer, updateWr);
    }

    @Override
    public boolean isOnline(Optional<ServerOnline> serverOpt) {

        if (serverOpt.isEmpty()
                || serverOpt.get().getStatus() == CommonConstants.LOGIN_STATUS_OFFLINE) {

            return false;
        }

        ServerOnline serverOnline = serverOpt.get();

        LocalDateTime lastPingTime = serverOnline.getLastPingTime();

        if (lastPingTime == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeoutTime = now.minusSeconds(SERVER_TIMEOUT_SECOND);

        // 超时
        if (timeoutTime.isAfter(lastPingTime)) {
            // 更新离线状态

            this.updateStatus(serverOnline.getServerId(), CommonConstants.LOGIN_STATUS_OFFLINE);
            serverOnline.setStatus(CommonConstants.LOGIN_STATUS_OFFLINE);

            return false;
        }

        return true;
    }

    @Override
    public void updateStatus(String serverId, Short status) {

        ServerOnline updateServer = new ServerOnline();
        //updateServer.setLastPingTime(LocalDateTime.now());
        updateServer.setStatus(status);

        UpdateWrapper<ServerOnline> updateWr = new UpdateWrapper<>();
        updateWr.eq("server_id", serverId);

        this.update(updateServer, updateWr);
    }

    private void clearServer(String serverId) {

        try {
            log.info("execute clear server task:{}", serverId);

            subjectOnlineService.offlineByServerId(serverId);
        } catch (Exception ex) {
            log.error("catch an exception when clear server task.", ex);
        }
    }

}