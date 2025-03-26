package org.noahsark.rpc.mq;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.common.remote.*;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.common.remote.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * MQ 默认消息处理器
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public class DefaultmqMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(DefaultmqMessageListener.class);

    private MqProxy proxy;

    private WorkQueue workQueue;

    private Dispatcher dispatcher;

    public DefaultmqMessageListener() {
    }

    public DefaultmqMessageListener(MqProxy proxy, WorkQueue workQueue, Dispatcher dispatcher) {
        this.proxy = proxy;
        this.workQueue = workQueue;
        this.dispatcher = dispatcher;
    }

    public DefaultmqMessageListener(MqProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean consumeMessage(byte[] message) {

        try {
            String msg = new String(message, StandardCharsets.UTF_8);
            JsonObject data = new JsonParser().parse(msg).getAsJsonObject();
            RpcCommand command;

            if (data.has("targets")) {
                command = JsonUtils.fromJson(msg, MqMultiRequest.class);
            } else {
                command = JsonUtils.fromJson(msg, Response.class);
            }

            logger.info("receive a command: {}", command);
            ChannelHolder channelHolder = proxy.getChannelHolder();
            PromiseHolder promiseHolder = proxy.getPromiseHolder();

            if (command.getType() == RpcCommand.REQUEST
                    || command.getType() == RpcCommand.ONEWAY) {

                RequestHandler.processRequest((MqMultiRequest) command, workQueue, dispatcher, channelHolder);

            } else if (command.getType() == RpcCommand.RESPONSE) {
                RequestHandler.processResponse(promiseHolder, command);
            }

            return true;
        } catch (Exception ex) {
            logger.error("Catch an exception in MessageListener.", ex);
        }

        return false;
    }
}
