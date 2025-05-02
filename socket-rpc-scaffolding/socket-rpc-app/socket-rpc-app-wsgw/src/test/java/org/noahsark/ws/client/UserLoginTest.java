package org.noahsark.ws.client;

import org.junit.Test;
import org.noahsark.common.cmd.Cmd;
import org.noahsark.common.constant.ClientTypeConstants;
import org.noahsark.event.PushGpsInfoEvent;
import org.noahsark.event.PushPowerEvent;
import org.noahsark.gw.ws.processor.login.dto.UserLoginDTO;
import org.noahsark.rpc.common.constant.WsConstants;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.DispatcherFactory;
import org.noahsark.rpc.common.remote.*;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.socket.ws.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/7/22
 */
public class UserLoginTest {

    private static Logger logger = LoggerFactory.getLogger(UserLoginTest.class);

    @Test
    public void loginTest() {

        String url = System.getProperty("url", "ws://192.168.3.108:9090/websocket");

        WebSocketClient client = new WebSocketClient(url);
        Dispatcher dispatcher = DispatcherFactory.getDispatcher(WsConstants.DEFAULT_DISPATCHER_NAME);
        client.setDispatcher(dispatcher);

        client.registerProcessor(new PushGpsProcessor());
        client.registerProcessor(new PushPowerProcessor());
        client.connect();

        Long tenantId = 1821382815037636609L;
        Long customerId = 1821383295130255362L;
        /*Long tenantId = 1766361213180350465L;
        Long customerId = 1769665143950643202L;*/
        Long userId = 1821383295201558529L;
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwcm9kdWN0SWQiOiJhcHBfMDEiLCJjdXN0b21lcklkIjoxODIxMzgzMjk1MTMwMjU1MzYyLCJleHAiOjE3NDY3NjExMTIsInR5cGUiOjMsInRpbWVzdGFtcCI6MTc0NjE1NjMxMjM5OH0.d7e_lpzVhgBBdlpYrN5OM3OBMEFB9cb5b7SFFauyJyk";

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setToken(token);
        userLoginDTO.setTenantId(tenantId);
        userLoginDTO.setCustomerId(customerId);
        userLoginDTO.setUserId(userId);
        userLoginDTO.setClientType(ClientTypeConstants.CLIENT_TYPE_WEB);

        try {

            TimeUnit.SECONDS.sleep(2);

            Request request = new Request.Builder()
                    .cmd(Cmd.CMD_USER_LOGIN)
                    .type(RpcCommand.REQUEST)
                    .seqId(1)
                    .data(userLoginDTO)
                    .build();

            client.invoke(request, new CommandCallback() {
                @Override
                public void callback(Object result, int currentFanout, int fanout) {
                    logger.info("login result = {} ", JsonUtils.toJson(result));
                }

                @Override
                public void failure(Throwable cause, int currentFanout, int fanout) {
                    logger.warn("Catch an exception.", cause);
                }
            }, 300000);

            TimeUnit.HOURS.sleep(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            client.shutdown();
        }

    }

    private static class PushGpsProcessor extends AbstractProcessor<PushGpsInfoEvent> {

        @Override
        protected void execute(PushGpsInfoEvent request, RpcContext context) {
            logger.info("Receive a PushGpsProcessor cmd:{}", JsonUtils.toJson(request));

            Response response = Response.buildCommonResponse(context.getCommand(), Response.SUCCESS, Response.SUCCESS_MESSAGE);

            context.getSession().write(response);
        }

        @Override
        protected Class<PushGpsInfoEvent> getParamsClass() {
            return PushGpsInfoEvent.class;
        }

        @Override
        protected int getCmd() {
            return Cmd.CMD_PUSH_GPS;
        }
    }

    private static class PushPowerProcessor extends AbstractProcessor<PushPowerEvent> {

        @Override
        protected void execute(PushPowerEvent request, RpcContext context) {
            logger.info("Receive a PushPowerProcessor cmd:{}", JsonUtils.toJson(request));

            Response response = Response.buildCommonResponse(context.getCommand(), Response.SUCCESS, Response.SUCCESS_MESSAGE);

            context.getSession().write(response);
        }

        @Override
        protected Class<PushPowerEvent> getParamsClass() {
            return PushPowerEvent.class;
        }

        @Override
        protected int getCmd() {
            return Cmd.CMD_PUSH_POWER;
        }
    }


}
