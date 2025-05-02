package org.noahsark.gw.ws.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.gw.ws.bootstrap.manager.OnlineManger;
import org.noahsark.gw.ws.bootstrap.subject.UserSubject;
import org.noahsark.gw.ws.service.IUserLogoutService;
import org.noahsark.online.pojo.po.SubjectLoginEvent;
import org.noahsark.online.service.ISubjectLoginEventService;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.rpc.common.constant.Cmd;
import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.socket.session.Session;
import org.noahsark.rpc.socket.session.SessionStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 强制下线服务实现
 *
 * @author zhangxt
 * @date 2024/05/27 16:36
 **/
@Service
public class UserLogoutServiceImpl implements IUserLogoutService {

    private static Logger log = LoggerFactory.getLogger(UserLogoutServiceImpl.class);

    @Autowired
    private ISubjectOnlineService subjectOnlineService;


    @Autowired
    private ISubjectLoginEventService subjectLoginEventService;

    @Override
    public boolean checkAndForceLogout(Short subjectType, String subjectId, String sessionId) {
        String clientType = OnlineManger.getInstance().getDefaultClientType(subjectType);

        return checkAndForceLogout(subjectType, subjectId, clientType, sessionId);
    }

    @Override
    public boolean checkAndForceLogout(Short subjectType, String subjectId, String clientType, String sessionId) {

        if (StringUtils.isEmpty(clientType)) {
            clientType = OnlineManger.getInstance().getDefaultClientType(subjectType);
        }

        Session session = OnlineManger.getInstance().getSession(subjectId, subjectType, clientType);

        if (session == null || !SessionStatusEnum.ONLINE.equals(session.getStatus())) {
            return false;
        }

        String currentSessionId = session.getSessionId();

        if (currentSessionId.equals(sessionId)) {

            log.info("{}/{} device/user repeat login.", subjectType, subjectId);

            session.setStatus(SessionStatusEnum.OFFLINE);

            return false;
        }

        log.info("{}/{} device/user has logged in and is now forced to log out.", subjectType, subjectId);

        // 1. 推送下线命令
        int cmd = Cmd.CMD_LOGOUT_OCCUPY;
        Request downwardReq = new Request.Builder()
                .cmd(cmd)
                .type(RpcCommand.ONEWAY)
                .seqId(session.getConnection().nextId())
                .data(null)
                .build();

        session.invokeOneway(downwardReq);

        // 2. 用户退出
        logout(subjectType, subjectId, session, CommonConstants.LOGOUT_OCCUPY, clientType);

        return true;
    }

    @Override
    public void logout(Short subjectType, String subjectId, Session session, Short logoutType) {
        String clientType = OnlineManger.getInstance().getDefaultClientType(subjectType);

        logout(subjectType, subjectId, session, logoutType, clientType);
    }

    @Override
    public void logout(Short subjectType, String subjectId, Session session, Short logoutType, String clientType) {

        if (StringUtils.isEmpty(clientType)) {
            clientType = OnlineManger.getInstance().getDefaultClientType(subjectType);
        }

        // 1. 设置用户下线
        subjectOnlineService.updateStatus(subjectId, subjectType, Short.parseShort(clientType), CommonConstants.LOGIN_STATUS_OFFLINE);

        // 2. 移除会话绑定关系
        OnlineManger.getInstance().removeSession(subjectId, subjectType, clientType);

        // 3. 记录用户下线事件
        UserSubject subject = (UserSubject) session.getSubject();
        if (subject != null) {

            SubjectLoginEvent loginEvent = new SubjectLoginEvent();

            loginEvent.setId(IdWorker.getId(loginEvent));
            loginEvent.setEventCause(logoutType);
            loginEvent.setCustomerId(subject.getCustomerId());
            loginEvent.setTenantId(subject.getTenantId());
            loginEvent.setSubjectType(subjectType);
            loginEvent.setSubjectId(subjectId);
            loginEvent.setEventType((short) 2);
            loginEvent.setEventTime(LocalDateTime.now());
            loginEvent.setClientType(Short.parseShort(clientType));

            subjectLoginEventService.save(loginEvent);

        }

        // 4. 清空缓存对象
        session.setSubject(null);
    }
}
