package org.noahsark.gw.ws.processor.login;

import org.apache.commons.lang3.StringUtils;
import org.noahsark.common.cmd.Cmd;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.common.constant.ResultConstants;
import org.noahsark.common.exception.CommonException;
import org.noahsark.gw.ws.bootstrap.manager.OnlineManger;
import org.noahsark.gw.ws.bootstrap.subject.UserSubject;
import org.noahsark.gw.ws.processor.login.dto.UserLoginDTO;
import org.noahsark.gw.ws.service.IUserLoginService;
import org.noahsark.gw.ws.service.IUserLogoutService;
import org.noahsark.online.pojo.po.SubjectOnline;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcContext;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.session.Session;
import org.noahsark.rpc.socket.session.SessionStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆处理器
 *
 * @author zhangxt
 * @date 2024/05/09 11:17
 **/
@Component
public class UserLoginProcessor extends AbstractProcessor<UserLoginDTO> {

    private static Logger log = LoggerFactory.getLogger(UserLoginProcessor.class);

    @Autowired
    private IUserLoginService userLoginService;

    @Autowired
    private IUserLogoutService userLogoutService;


    @Autowired()
    @Qualifier("commonExecutor")
    private ThreadPoolTaskExecutor commonExecutor;

    @Override
    protected void execute(UserLoginDTO request, RpcContext context) {

        Session newSession = (Session) context.getSession();

        log.info("receive a admin login request: {}/{}", JsonUtils.toJson(request), newSession.getChannel());

        Response response = Response.defaultResponse(context.getCommand());

        try {

            // 判断用户是否在线
            Short subjectType = CommonConstants.SUBJECT_USER;
            String subjectId = request.getUserId().toString();
            String sessionId = request.getSessionId();
            String clientType;
            if (request.getClientType() == null) {
                clientType = OnlineManger.getInstance().getDefaultClientType(subjectType);
            } else {
                clientType = request.getClientType().toString();
            }

            request.setClientType(Short.valueOf(clientType));

            // 强制退出
            userLogoutService.checkAndForceLogout(subjectType, subjectId, clientType, sessionId);

            // 1. 用户登陆
            userLoginService.userLogin(request);

            // 2. 写入在线用户表
            SubjectOnline subject = getSubjectOnline(request);
            userLoginService.updateOnlineSubject(subject);

            // 3. 绑定会话
            bindSession(subject, context, sessionId);

            Session session = OnlineManger.getInstance().getSession(subjectId, subjectType, clientType);
            Map<String, String> data = new HashMap<>();
            data.put("sessionId", session.getSessionId());

            log.info("clientType:{}", clientType);

            response = Response.buildResponse(context.getCommand(), data, Response.SUCCESS, Response.SUCCESS_MESSAGE);

        } catch (CommonException cex) {
            log.error("catch an exception when admin login request.", cex);

            response = Response.buildCommonResponse(context.getCommand(), cex.getCode(), cex.getMessage());
        } catch (Exception ex) {
            log.error("catch an exception when admin login request.", ex);

            response = Response.buildCommonResponse(context.getCommand(),
                    ResultConstants.FAIL_CODE, ResultConstants.FAIL_MSG);
        }

        // 返回结果
        context.sendResponse(response);

    }

    @Override
    protected Class<UserLoginDTO> getParamsClass() {
        return UserLoginDTO.class;
    }

    @Override
    protected int getCmd() {
        return Cmd.CMD_USER_LOGIN;
    }

    private void bindSession(SubjectOnline subjectOnline, RpcContext context, String sessionId) {

        String subjectId = subjectOnline.getSubjectId();
        short type = CommonConstants.SUBJECT_USER;
        String clientType = String.valueOf(subjectOnline.getClientType());

        cleanOldSession(subjectId, type, clientType, sessionId);

        Session newSession = (Session) context.getSession();
        Session session = OnlineManger.getInstance().checkOrRecoverSession(subjectId, type, clientType, newSession, sessionId);
        session.setStatus(SessionStatusEnum.ONLINE);

        UserSubject subject;

        if (newSession == session) {
            subject = new UserSubject(subjectId, type);
            subject.setTenantId(subjectOnline.getTenantId());
            subject.setCustomerId(subjectOnline.getCustomerId());
            subject.setClientType(subjectOnline.getClientType());
            subject.setSessionId(session.getSessionId());

            session.setSubject(subject);
        } else {
            subject = (UserSubject) session.getSubject();
        }

        // 推送缓存的消息userLogoutService
        session.processPendingMsgs();

    }

    private void cleanOldSession(String subjectId, short type, String clientType, String sessionId) {
        Session oldSession = OnlineManger.getInstance().getSession(subjectId, type, clientType);

        if (oldSession == null) {
            return;
        }

        String currentSessionId = oldSession.getSessionId();

        UserSubject subject = (UserSubject) oldSession.getSubject();

        if (subject == null) {
            return;
        }

        if (!currentSessionId.equals(sessionId)) {

            String callId = subject.getCallId();

            if (!StringUtils.isEmpty(callId)) {

                subject.setCallId(null);
            }
        }

    }

    private SubjectOnline getSubjectOnline(UserLoginDTO request) {
        Long tenantId = request.getTenantId();
        Long customerId = request.getCustomerId();

        SubjectOnline subject = new SubjectOnline();
        subject.setType(CommonConstants.SUBJECT_USER);
        subject.setSubjectId(String.valueOf(request.getUserId()));
        subject.setClientType(request.getClientType());
        subject.setTenantId(tenantId);
        subject.setCustomerId(customerId);
        return subject;
    }
}
