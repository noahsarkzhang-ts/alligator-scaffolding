package org.noahsark.gw.ws.bootstrap.busevent;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.gw.ws.bootstrap.subject.UserSubject;
import org.noahsark.online.pojo.po.SubjectLoginEvent;
import org.noahsark.online.service.ISubjectLoginEventService;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.event.ClientDisconnectEvent;
import org.noahsark.rpc.socket.eventbus.ApplicationListener;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * @author: zhangxt
 * @desc: 客户端下线监听器
 * @version:
 * @date: 2021/7/21
 */
@Component
public class ClientDisconnectListener extends ApplicationListener<ClientDisconnectEvent> {

    private static Logger log = LoggerFactory.getLogger(ClientDisconnectListener.class);

    @Autowired
    private ISubjectOnlineService subjectOnlineService;

    @Autowired
    private ISubjectLoginEventService subjectLoginEventService;

    @Override
    public void onApplicationEvent(ClientDisconnectEvent event) {

        try {
            UserSubject subject = (UserSubject) event.getSource();

            if (subject == null) {
                log.info("Receive user offline event: subject is null");
                return;
            }
            log.info("Receive user offline event:{}", JsonUtils.toJson(subject));

            // 1. 记录用户下线事件
            SubjectLoginEvent loginEvent = new SubjectLoginEvent();

            loginEvent.setId(IdWorker.getId(loginEvent));
            loginEvent.setEventCause(CommonConstants.LOGOUT_UNCONNECTED);
            loginEvent.setCustomerId(subject.getCustomerId());
            loginEvent.setTenantId(subject.getTenantId());
            loginEvent.setSubjectType(subject.getType());
            loginEvent.setSubjectId(subject.getSubjectId());
            loginEvent.setClientType(subject.getClientType());
            loginEvent.setEventType((short) 2);
            loginEvent.setEventTime(LocalDateTime.now());

            subjectLoginEventService.save(loginEvent);

        } catch (Exception ex) {
            log.error("Exception occurred when handling user offline event:{}",
                    JsonUtils.toJson(event.getSource()), ex);
        }

    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }


}
