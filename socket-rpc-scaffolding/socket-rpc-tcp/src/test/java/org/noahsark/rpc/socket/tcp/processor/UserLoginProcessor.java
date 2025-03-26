package org.noahsark.rpc.socket.tcp.processor;

import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcContext;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.tcp.pojo.Constants;
import org.noahsark.rpc.socket.tcp.pojo.TokenInfo;
import org.noahsark.rpc.socket.tcp.pojo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 用户登录处理器
 *
 * @author zhangxt
 * @date 2025/03/22 19:57
 **/
public class UserLoginProcessor extends AbstractProcessor<UserInfo> {

    private static Logger logger = LoggerFactory.getLogger(UserLoginProcessor.class);

    @Override
    protected void execute(UserInfo request, RpcContext context) {

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserName(request.getUserName());
        tokenInfo.setToken(UUID.randomUUID().toString());

        Response response = Response.buildResponse(context.getCommand(), tokenInfo, 1, "success");

        logger.info("Send response: {} ", JsonUtils.toJson(response));

        context.sendResponse(response);
    }

    @Override
    protected Class<UserInfo> getParamsClass() {
        return UserInfo.class;
    }

    @Override
    protected int getCmd() {
        return 1000;
    }

    @Override
    protected String getDispatcherName() {
        return Constants.DEFAULT_DISPATCHER_NAME;
    }
}
