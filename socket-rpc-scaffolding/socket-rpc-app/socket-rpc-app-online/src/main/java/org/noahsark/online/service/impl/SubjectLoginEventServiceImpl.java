package org.noahsark.online.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.noahsark.online.mapper.SubjectLoginEventMapper;
import org.noahsark.online.pojo.po.SubjectLoginEvent;
import org.noahsark.online.service.ISubjectLoginEventService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登陆事件 服务实现类
 * </p>
 *
 * @author allen
 * @since 2024-05-27
 */
@Service
public class SubjectLoginEventServiceImpl extends ServiceImpl<SubjectLoginEventMapper, SubjectLoginEvent> implements ISubjectLoginEventService {
}