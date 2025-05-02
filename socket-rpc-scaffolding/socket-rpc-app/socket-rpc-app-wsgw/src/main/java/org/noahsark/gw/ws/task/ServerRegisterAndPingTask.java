package org.noahsark.gw.ws.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.noahsark.gw.ws.bootstrap.config.CommonConfig;
import org.noahsark.online.pojo.po.ServerOnline;
import org.noahsark.online.service.IServerOnlineService;
import org.noahsark.rpc.common.util.CollectionsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 网关注册及心跳定时任务
 *
 * @author zhangxt
 * @date 2024/03/19 18:08
 **/
@Component
public class ServerRegisterAndPingTask {

    private static Logger log = LoggerFactory.getLogger(ServerRegisterAndPingTask.class);

    @Autowired
    private CommonConfig config;

    @Autowired
    private IServerOnlineService serverOnlineService;

    // 心跳：30秒
    private static final int DELAY = 30 * 1000;

    @Scheduled(fixedRate = DELAY, initialDelay = DELAY)
    public void start() {
        log.info("Server register or ping...");

        String serverId = config.getServerId();
        Optional<ServerOnline> opt = getSeverOnline(serverId);

        if (!register(opt)) {
            ping(opt);
        }
    }

    private Optional<ServerOnline> getSeverOnline(String serverId) {
        QueryWrapper<ServerOnline> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_id", serverId);

        List<ServerOnline> list = serverOnlineService.list(queryWrapper);

        if (!CollectionsUtils.isEmpty(list)) {
            return Optional.of(list.get(0));
        } else {
            return Optional.ofNullable(null);
        }

    }

    private boolean register(Optional<ServerOnline> opt) {

        if (opt.isEmpty()) {
            // 注册
            ServerOnline server = new ServerOnline();
            server.setId(IdWorker.getId(server));
            server.setServerId(config.getServerId());
            server.setTopic(config.getMqTopic());
            server.setStatus((short) 1);
            server.setLoginTime(LocalDateTime.now());
            server.setLastPingTime(LocalDateTime.now());

            serverOnlineService.save(server);

            return true;
        }

        return false;
    }

    private void ping(Optional<ServerOnline> opt) {

        if (opt.isPresent()) {
            ServerOnline current = opt.get();

            ServerOnline updateServer = new ServerOnline();
            updateServer.setLastPingTime(LocalDateTime.now());
            updateServer.setStatus((short) 1);

            Long id = current.getId();
            UpdateWrapper<ServerOnline> updateWr = new UpdateWrapper<>();
            updateWr.eq("id", id);

            serverOnlineService.update(updateServer, updateWr);
        }
    }
}
