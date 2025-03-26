package org.noahsark.gw.ws.bootstrap.busevent;

import lombok.extern.slf4j.Slf4j;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.gw.ws.bootstrap.manager.OnlineManger;
import org.noahsark.gw.ws.bootstrap.subject.UserSubject;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.event.SessionOfflineEvent;
import org.noahsark.rpc.socket.eventbus.ApplicationListener;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.noahsark.rpc.socket.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: zhangxt
 * @desc: 客户端下线监听器
 * @version:
 * @date: 2021/7/21
 */
@Component
@Slf4j
public class SessionOfflineListener extends ApplicationListener<SessionOfflineEvent> {

    @Autowired
    private ISubjectOnlineService subjectOnlineService;

    @Override
    public void onApplicationEvent(SessionOfflineEvent event) {

        try {
            UserSubject subject = (UserSubject) event.getSource();

            if (subject == null) {
                log.info("Receive session offline event: subject is null");
                return;
            }
            log.info("Receive session offline event:{}", JsonUtils.toJson(subject));

            String oldSessionId = subject.getSessionId();
            Short clientType = subject.getClientType();
            String sclientType = clientType == null ? "" : String.valueOf(clientType);

            Session currentSession = OnlineManger.getInstance().getSession(subject.getSubjectId(), subject.getType(), sclientType);
            if (currentSession == null) {
                log.info("Session is null:{}", oldSessionId);

                return;
            }

            String currentSessionId = currentSession.getSessionId();
            if (!currentSessionId.equals(oldSessionId)) {
                log.info("No match of session:{}/{}", oldSessionId, currentSessionId);

                return;
            }

            // 1. 设置用户离线
            subjectOnlineService.updateStatus(subject.getSubjectId(),
                    subject.getType(), clientType, CommonConstants.LOGIN_STATUS_OFFLINE);

            // 2. 移除会话绑定关系
            OnlineManger.getInstance().removeSession(subject.getSubjectId(), subject.getType(), sclientType);

        } catch (Exception ex) {
            log.error("Exception occurred when handling session offline event:{}",
                    JsonUtils.toJson(event.getSource()), ex);
        }

    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }


}
