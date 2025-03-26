package org.noahsark.rpc.common.remote;

import org.noahsark.rpc.common.constant.Cmd;
import org.noahsark.rpc.common.dispatcher.AbstractProcessor;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class RequestHandler {

    private static Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * 处理请求
     *
     * @param command   请求
     * @param workQueue 工作队列
     * @param session   会话
     */
    public static void processRequest(Request command, WorkQueue workQueue, Dispatcher dispatcher, ChannelHolder session) {

        Response response;

        try {
            RpcContext rpcContext = new RpcContext.Builder()
                    .command(command)
                    .session(session)
                    .build();

            RpcRequest rpcRequest = new RpcRequest.Builder()
                    .request(command)
                    .context(rpcContext)
                    .build();

            if (workQueue.isBusy()) {
                log.info("service is busy: {}", JsonUtils.toJson(command));

                response = command.build(Response.FAIL,"service is busy！");

                session.write(response);

            } else {
                workQueue.add(() -> {

                    String processName;
                    AbstractProcessor processor;

                    if (command.multiEnable()) {
                        processName = ":" + Cmd.CMD_MQ_DOWNWARD;
                        processor = dispatcher.getProcessor(processName);
                        // log.info("processName:", processName);

                        if (processor != null) {
                            processor.process(rpcRequest);

                            return;
                        }
                    }

                    processName = ":" + command.getCmd();
                    // log.info("processName: {}", processName);

                    processor = dispatcher.getProcessor(processName);

                    if (processor != null) {
                        processor.process(rpcRequest);
                    } else {

                        // 使用默认的处理器
                        processName = ":" + -1;
                        processor = dispatcher.getProcessor(processName);

                        if (processor != null) {
                            processor.process(rpcRequest);
                        } else {
                            log.warn("No processor: {}", processName);
                            Response res = command.build(Response.FAIL,"No processor for msgId！");

                            session.write(res);
                        }
                    }

                });
            }

            return;
        } catch (Exception ex) {
            log.warn("catch an exception:{}", ex);

            response = command.build(Response.FAIL,"System exception!");
        }

        session.write(response);
    }

    /**
     * 处理响应
     *
     * @param connection 连接
     * @param command    命令
     */
    public static void processResponse(PromiseHolder connection, RpcCommand command) {
        RpcPromise promise = connection.getPromise(command.getSeqId());

        if (promise != null) {
            // Response上有code、msg，需要返回使用
            promise.setSuccess(command);

        } else {
            log.warn("promise is null : {}", command.getSeqId());
        }
    }
}
