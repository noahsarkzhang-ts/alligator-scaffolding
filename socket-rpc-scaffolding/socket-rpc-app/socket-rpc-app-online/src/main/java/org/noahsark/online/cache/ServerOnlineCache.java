package org.noahsark.online.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.noahsark.common.exception.CommonException;
import org.noahsark.online.mapper.ServerOnlineMapper;
import org.noahsark.online.pojo.po.ServerOnline;
import org.noahsark.util.CollectionsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 服务在线缓存
 *
 * @author zhangxt
 * @date 2024/03/19 17:09
 **/
@Component
public class ServerOnlineCache {

    private static Logger log = LoggerFactory.getLogger(ServerOnlineCache.class);

    @Autowired
    private ServerOnlineMapper mapper;

    private LoadingCache<String, Optional<ServerOnline>> cache;

    public ServerOnlineCache() {
    }

    @PostConstruct
    private void init() {

        CacheLoader<String, Optional<ServerOnline>> loader = new CacheLoader<>() {

            @Override
            public Optional<ServerOnline> load(String key) throws Exception {

                QueryWrapper<ServerOnline> wrapper = new QueryWrapper<>();
                wrapper.eq("server_id", key);

                List<ServerOnline> list = mapper.selectList(wrapper);

                if (!CollectionsUtils.isEmpty(list)) {
                    return Optional.of(list.get(0));
                } else {
                    return Optional.ofNullable(null);
                }
            }
        };

        this.cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(5, TimeUnit.SECONDS).build(loader);

    }

    public Optional<ServerOnline> get(String serverId) {
        try {

            Optional<ServerOnline> serverOnline = cache.get(serverId);

            return serverOnline;
        } catch (Exception ex) {
            log.warn("Catch an exception when get server online info:", ex);

            throw new CommonException(-1, ex.getMessage());
        }
    }

}
