package org.noahsark.online.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.noahsark.common.constant.ClientTypeConstants;
import org.noahsark.common.exception.CommonException;
import org.noahsark.online.mapper.SubjectOnlineMapper;
import org.noahsark.online.pojo.po.SubjectOnline;
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
 * 用户在线缓存
 *
 * @author zhangxt
 * @date 2024/03/19 17:31
 **/
@Component
public class SubjectOnlineCache {

    private static Logger log = LoggerFactory.getLogger(SubjectOnlineCache.class);

    @Autowired
    private SubjectOnlineMapper mapper;

    private LoadingCache<String, Optional<SubjectOnline>> cache;

    public SubjectOnlineCache() {
    }

    @PostConstruct
    private void init() {

        CacheLoader<String, Optional<SubjectOnline>> loader = new CacheLoader<>() {

            @Override
            public Optional<SubjectOnline> load(String key) throws Exception {

                String[] params = key.split("::");

                QueryWrapper<SubjectOnline> wrapper = new QueryWrapper<>();
                wrapper.eq("subject_id", params[0]);
                wrapper.eq("type", Integer.parseInt(params[1]));
                wrapper.eq("client_type", Integer.parseInt(params[2]));

                List<SubjectOnline> list = mapper.selectList(wrapper);

                if (!CollectionsUtils.isEmpty(list)) {
                    return Optional.of(list.get(0));
                } else {
                    return Optional.ofNullable(null);
                }
            }
        };

        this.cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(5, TimeUnit.SECONDS).build(loader);

    }

    public Optional<SubjectOnline> get(String subjectId, Short type) {
        Short clientType = ClientTypeConstants.getDefaultClientType(type);

        return get(subjectId, type, clientType);
    }

    public Optional<SubjectOnline> get(String subjectId, Short type, Short clientType) {
        try {

            if (ClientTypeConstants.isEmpty(clientType)) {
                clientType = ClientTypeConstants.getDefaultClientType(type);
            }

            String key = String.format("%s::%d::%d", subjectId, type, clientType);

            Optional<SubjectOnline> subjectOnline = cache.get(key);

            return subjectOnline;
        } catch (Exception ex) {
            log.warn("Catch an exception when get subject online info:", ex);

            throw new CommonException(-1, ex.getMessage());
        }
    }
}
