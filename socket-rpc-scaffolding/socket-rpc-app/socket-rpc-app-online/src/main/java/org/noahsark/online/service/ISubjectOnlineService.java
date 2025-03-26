package org.noahsark.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.noahsark.online.pojo.po.SubjectOnline;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户在线 服务类
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
public interface ISubjectOnlineService extends IService<SubjectOnline> {

    Optional<SubjectOnline> getOne(String subjectId, short type);

    Optional<SubjectOnline> getOne(String subjectId, short type, short clientType);

    void updateStatus(String subjectId, short type, short status);

    void updateStatus(String subjectId, short type, short clientType, short status);

    List<SubjectOnline> getSubjectOfCustomerId(Long tenantId, Long customerId, Short type);

    List<SubjectOnline> getSubjectOfCustomerId(Long tenantId, Long customerId, Short type, Short clientType);

    List<SubjectOnline> getOnlineSubjects(String subjectId, short type);

    void offlineByServerId(String serverId);

    List<SubjectOnline> getOnlineSubjects(Short type);
}