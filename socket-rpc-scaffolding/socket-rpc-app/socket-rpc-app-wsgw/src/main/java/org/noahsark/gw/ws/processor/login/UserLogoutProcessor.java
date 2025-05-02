package org.noahsark.gw.ws.processor.login;

import org.noahsark.common.cmd.Cmd;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.common.constant.ResultConstants;
import org.noahsark.gw.ws.bootstrap.manager.OnlineManger;
import org.noahsark.gw.ws.bootstrap.subject.UserSubject;
import org.noahsark.gw.ws.processor.login.dto.UserLogoutDTO;
import org.noahsark.gw.ws.service.IUserLogoutService;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcContext;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.session.Session;
import org.noahsark.rpc.socket.session.SessionStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户退出处理器
 *
 * @author zhangxt
 * @date 2024/05/09 11:30
 **/
@Component
public class UserLogoutProcessor extends AbstractProcessor<UserLogoutDTO> {

    private static Logger log = LoggerFactory.getLogger(UserLogoutProcessor.class);

    @Autowired
    private IUserLogoutService userLogoutService;

    @Override
    protected void execute(UserLogoutDTO request, RpcContext context) {

        log.info("Receive user offline event:{}", JsonUtils.toJson(request));

        Response response = Response.defaultResponse(context.getCommand());

        try {

            String subjectId = String.valueOf(request.getUserId());
            Short type = CommonConstants.SUBJECT_USER;
            Session session = (Session) context.getSession();
            session.setStatus(SessionStatusEnum.LOGOUT);

            UserSubject subject = (UserSubject) session.getSubject();

            String clientType;
            if (subject != null) {
                clientType = String.valueOf(subject.getClientType());
            } else {
                clientType = OnlineManger.getInstance().getDefaultClientType(type);
            }

            userLogoutService.logout(type, subjectId, session, CommonConstants.LOGOUT_NORMAL, clientType);

        } catch (Exception ex) {
            log.error("Exception occurred when handling user offline event", ex);
            response = Response.buildCommonResponse(context.getCommand(),
                    ResultConstants.FAIL_CODE, ResultConstants.FAIL_MSG);
        }

        // 返回结果
        context.sendResponse(response);
    }

    @Override
    protected Class<UserLogoutDTO> getParamsClass() {
        return UserLogoutDTO.class;
    }

    @Override
    protected int getCmd() {
        return Cmd.CMD_USER_LOGOUT;
    }
}
