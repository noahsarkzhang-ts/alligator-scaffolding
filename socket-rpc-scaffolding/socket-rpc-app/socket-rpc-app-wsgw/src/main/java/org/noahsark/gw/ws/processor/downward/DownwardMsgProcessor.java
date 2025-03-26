package org.noahsark.gw.ws.processor.downward;

import org.noahsark.common.cmd.Cmd;
import org.noahsark.common.constant.ClientTypeConstants;
import org.noahsark.common.constant.ResultConstants;
import org.noahsark.gw.ws.bootstrap.config.CommonConfig;
import org.noahsark.gw.ws.bootstrap.manager.OnlineManger;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.exception.TimeoutException;
import org.noahsark.rpc.common.remote.*;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.mq.MqMultiRequest;
import org.noahsark.rpc.mq.MqMultiResponse;
import org.noahsark.rpc.socket.session.Connection;
import org.noahsark.rpc.socket.session.Session;
import org.noahsark.util.CollectionsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MQ RPC处理器
 *
 * @author zhangxt
 * @date 2024/05/07 21:04
 **/
@Component
public class DownwardMsgProcessor extends AbstractProcessor<Void> {

    private static Logger log = LoggerFactory.getLogger(DownwardMsgProcessor.class);

    @Autowired
    private CommonConfig config;

    @Override
    protected void execute(Void request, RpcContext context) {

        try {

            MqMultiRequest req = (MqMultiRequest) context.getCommand();

            List<MqMultiRequest.EndUser> targets = req.getTargets();
            final int cmd = req.getCmd();
            final Object data = req.getData();

            boolean needResponse = (req.getType() == RpcCommand.REQUEST);
            final MultiPromiseHolder promiseHolder = new MultiPromiseHolder();
            promiseHolder.setNeedResponse(needResponse);

            MultiRpcPromise multiPromise = null;

            if (needResponse) {
                multiPromise = new MultiRpcPromise.Builder()
                        .num(targets.size())
                        .model(MultiRpcPromise.RpcModel.ALL)
                        .timeoutMillis(150 * 1000)
                        .callback(new CommandCallback() {
                            @Override
                            public void callback(Object result, int currentFanout, int fanout) {
                                // TODO
                                int code = Response.SUCCESS;
                                String msg = Response.SUCCESS_MESSAGE;

                                MqMultiResponse response = new MqMultiResponse.Builder()
                                        .seqId(req.getSeqId())
                                        .cmd(req.getCmd())
                                        .type(RpcCommand.RESPONSE)
                                        .code(code)
                                        .msg(msg)
                                        .data(result)
                                        .topic(req.getRepliedTopic())
                                        .build();

                                context.getSession().write(response);
                            }

                            @Override
                            public void failure(Throwable cause, int currentFanout, int fanout) {
                                // TODO
                                int code = ResultConstants.REQUEST_EXCEPTION_CODE;
                                String msg = "System exception";

                                if (cause instanceof TimeoutException) {
                                    code = ResultConstants.TIMEOUT_CODE;
                                    msg = "Request timeout";
                                }

                                MqMultiResponse response = new MqMultiResponse.Builder()
                                        .seqId(req.getSeqId())
                                        .cmd(req.getCmd())
                                        .type(RpcCommand.RESPONSE)
                                        .code(code)
                                        .msg(msg)
                                        .data(null)
                                        .topic(req.getRepliedTopic())
                                        .build();

                                context.getSession().write(response);

                            }
                        })
                        .build();

                promiseHolder.setMultiPromise(multiPromise);
            }

            targets.forEach(endUser -> {
                String subjectId = endUser.getUserId();
                Short subjectType = endUser.getType();
                Short clientType = endUser.getClientType();

                try {

                    String requestId = String.format("%d_%d_%s", clientType, subjectType, subjectId);

                    if (needResponse) {

                        if (ClientTypeConstants.isEmpty(clientType)) {
                            clientType = ClientTypeConstants.getDefaultClientType(subjectType);
                        }

                        Session session = OnlineManger.getInstance().getSession(subjectId, subjectType, String.valueOf(clientType));

                        if (session == null) {
                            log.info("User is offline:{}/{}/{}", clientType, subjectType, subjectId);

                            return;
                        }

                        Connection connection = session.getConnection();

                        Request downwardReq = new Request.Builder()
                                .cmd(cmd)
                                .type(req.getType())
                                .seqId(connection.nextId())
                                .data(data)
                                .build();

                        session.invoke(downwardReq, new CommandCallback() {
                            @Override
                            public void callback(Object result, int currentFanout, int fanout) {
                                log.info("Receive success response of downward msg: {} / {} / {} , result: {} .", subjectId,
                                        cmd, JsonUtils.toJson(data), JsonUtils.toJson(result));

                                Response msg = (Response) result;

                                if (promiseHolder.needResponse && promiseHolder.multiPromise != null) {
                                    if (msg.getCode() == 0) {
                                        promiseHolder.multiPromise.setSuccess(requestId, result);
                                    } else {
                                        promiseHolder.multiPromise.setFailure(requestId, result);
                                    }

                                }
                            }

                            @Override
                            public void failure(Throwable cause, int currentFanout, int fanout) {
                                log.error("Receive fail response of downward msg: {} / {} / {}, result: {} .", subjectId,
                                        cmd, JsonUtils.toJson(data), cause.getMessage());
                                log.error("Exception", cause);

                                if (promiseHolder.needResponse && promiseHolder.multiPromise != null) {

                                    int code = ResultConstants.REQUEST_EXCEPTION_CODE;
                                    String msg = "System exception";

                                    if (cause instanceof TimeoutException) {
                                        code = ResultConstants.TIMEOUT_CODE;
                                        msg = "Request timeout";
                                    }

                                    Response response = Response.buildResponse(downwardReq, null, code, msg);

                                    promiseHolder.multiPromise.setFailure(requestId, response);
                                }
                            }
                        }, 120 * 1000);
                    } else {

                        if (!ClientTypeConstants.isEmpty(clientType)) {

                            Session session = OnlineManger.getInstance().getSession(subjectId, subjectType, String.valueOf(clientType));

                            if (session == null) {
                                log.info("User is offline:{}/{}/{}", clientType, subjectType, subjectId);

                                return;
                            }

                            Connection connection = session.getConnection();

                            Request downwardReq = new Request.Builder()
                                    .cmd(cmd)
                                    .type(req.getType())
                                    .seqId(connection.nextId())
                                    .data(data)
                                    .build();

                            session.invokeOneway(downwardReq);

                            return;
                        }

                        // 一个用户登陆多个端，同时推送
                        List<Session> sessions = OnlineManger.getInstance().getSessions(subjectId, subjectType);
                        if (CollectionsUtils.isEmpty(sessions)) {
                            log.info("User is offline:{}/{}/{}", subjectType, subjectId);

                            return;
                        }

                        sessions.forEach(session -> {

                            Connection connection = session.getConnection();

                            Request downwardReq = new Request.Builder()
                                    .cmd(cmd)
                                    .type(req.getType())
                                    .seqId(connection.nextId())
                                    .data(data)
                                    .build();

                            session.invokeOneway(downwardReq);
                        });

                    }

                    if (promiseHolder.needResponse && promiseHolder.multiPromise != null) {
                        promiseHolder.multiPromise.addRequest(requestId);
                    }
                } catch (Exception ex) {
                    log.error("Send downward message fail.", ex);
                }

            });
        } catch (Exception ex) {
            log.error("Catch an exception when process mq msg.", ex);
        }

    }

    @Override
    protected Class<Void> getParamsClass() {
        return Void.class;
    }

    @Override
    protected int getCmd() {
        return Cmd.CMD_MQ_DOWNWARD;
    }

    public static class MultiPromiseHolder {
        private boolean needResponse = false;

        private MultiRpcPromise multiPromise;

        public boolean isNeedResponse() {
            return needResponse;
        }

        public void setNeedResponse(boolean needResponse) {
            this.needResponse = needResponse;
        }

        public MultiRpcPromise getMultiPromise() {
            return multiPromise;
        }

        public void setMultiPromise(MultiRpcPromise multiPromise) {
            this.multiPromise = multiPromise;
        }
    }
}
