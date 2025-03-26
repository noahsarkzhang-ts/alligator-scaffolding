package org.noahsark.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.online.mapper.SubjectOnlineMapper;
import org.noahsark.online.pojo.mapstruct.SubjectOnlineMapstruct;
import org.noahsark.online.pojo.po.SubjectOnline;
import org.noahsark.online.pojo.vo.SubjectOnlineVO;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.util.CollectionsUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户在线 服务实现类
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
@Service
public class SubjectOnlineServiceImpl extends ServiceImpl<SubjectOnlineMapper, SubjectOnline>
        implements ISubjectOnlineService {

    @Override
    public Optional<SubjectOnline> getOne(String subjectId, short type) {

        QueryWrapper<SubjectOnline> wrapper = new QueryWrapper<>();
        wrapper.eq("subject_id", subjectId);
        wrapper.eq("type", type);

        List<SubjectOnline> list = getBaseMapper().selectList(wrapper);

        if (!CollectionsUtils.isEmpty(list)) {
            return Optional.of(list.get(0));
        } else {
            return Optional.ofNullable(null);
        }

    }

    @Override
    public Optional<SubjectOnline> getOne(String subjectId, short type, short clientType) {
        QueryWrapper<SubjectOnline> wrapper = new QueryWrapper<>();
        wrapper.eq("subject_id", subjectId);
        wrapper.eq("type", type);
        wrapper.eq("client_type", clientType);

        List<SubjectOnline> list = getBaseMapper().selectList(wrapper);

        if (!CollectionsUtils.isEmpty(list)) {
            return Optional.of(list.get(0));
        } else {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public void updateStatus(String subjectId, short type, short status) {
        SubjectOnline subject = new SubjectOnline();
        subject.setStatus(status);

        UpdateWrapper<SubjectOnline> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("subject_id", subjectId);
        updateWrapper.eq("type", type);

        // 更新状态
        getBaseMapper().update(subject, updateWrapper);
    }

    @Override
    public void updateStatus(String subjectId, short type, short clientType, short status) {
        SubjectOnline subject = new SubjectOnline();
        subject.setStatus(status);

        UpdateWrapper<SubjectOnline> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("subject_id", subjectId);
        updateWrapper.eq("type", type);
        updateWrapper.eq("client_type", clientType);

        // 更新状态
        getBaseMapper().update(subject, updateWrapper);
    }

    @Override
    public List<SubjectOnline> getSubjectOfCustomerId(Long tenantId, Long customerId, Short type) {
        QueryWrapper<SubjectOnline> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("customer_id", customerId);
        wrapper.eq("status", (short) 1);
        wrapper.eq("type", type);

        List<SubjectOnline> list = getBaseMapper().selectList(wrapper);

        return list;
    }

    @Override
    public List<SubjectOnline> getSubjectOfCustomerId(Long tenantId, Long customerId, Short type, Short clientType) {
        QueryWrapper<SubjectOnline> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("customer_id", customerId);
        wrapper.eq("status", (short) 1);
        wrapper.eq("type", type);
        wrapper.eq("client_type", clientType);

        List<SubjectOnline> list = getBaseMapper().selectList(wrapper);

        return list;
    }

    @Override
    public List<SubjectOnline> getOnlineSubjects(String subjectId, short type) {
        QueryWrapper<SubjectOnline> wrapper = new QueryWrapper<>();
        wrapper.eq("subject_id", subjectId);
        wrapper.eq("type", type);
        wrapper.eq("status", (short) 1);

        List<SubjectOnline> list = getBaseMapper().selectList(wrapper);

        return list;
    }

    @Override
    public void offlineByServerId(String serverId) {
        SubjectOnline subject = new SubjectOnline();
        subject.setStatus(CommonConstants.LOGIN_STATUS_OFFLINE);

        UpdateWrapper<SubjectOnline> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("server_id", serverId);

        // 更新状态
        getBaseMapper().update(subject, updateWrapper);
    }

    @Override
    public List<SubjectOnline> getOnlineSubjects(Short type) {
        LambdaQueryWrapper<SubjectOnline> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SubjectOnline::getType, type);
        wrapper.eq(SubjectOnline::getStatus, (short) 1);
        return list(wrapper);
    }

    private List<SubjectOnlineVO> toVoList(List<SubjectOnline> poList) {
        return poList.stream().map(deviceChannelInfo -> SubjectOnlineMapstruct.INSTANCE.toVO(deviceChannelInfo))
                .collect(Collectors.toList());
    }
}