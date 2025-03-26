package org.noahsark.rpc.mq.nats;

import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.util.JsonUtils;
import org.noahsark.rpc.mq.*;
import org.noahsark.rpc.mq.nats.manager.NatsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * RabbitMQ 生产者
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class NatsmqProducer implements Producer<NatsmqMessage, NatsmqSendResult> {

    private static Logger logger = LoggerFactory.getLogger(NatsmqProducer.class);

    public NatsmqProducer() {
    }

    @Override
    public void send(NatsmqMessage msg, SendCallback sendCallback, long timeout) {

        try {
            // 发送消息到 mq
            NatsManager.getInstance().publish(msg.getTopic(), msg.getContent());

        } catch (Exception ex) {
            logger.warn("catch an exception when sending mssage:", ex);
        }

    }

    @Override
    public NatsmqMessage buildMessage(RpcCommand command) {

        NatsmqMessage msg = new NatsmqMessage();
        String topic;

        if (command instanceof MqMultiRequest) {
            topic = ((MqMultiRequest) command).getTopic();
        } else {
            topic = ((MqMultiResponse) command).getTopic();
        }

        msg.setTopic(topic);
        byte[] body = JsonUtils.toJson(command).getBytes(StandardCharsets.UTF_8);

        msg.setContent(body);
        return msg;
    }

    @Override
    public NatsmqSendResult send(NatsmqMessage msg) {


        NatsmqSendResult result = new NatsmqSendResult();

        result.setSuccess(false);
        try {

            // 发送消息到 mq
            NatsManager.getInstance().publish(msg.getTopic(), msg.getContent());
            result.setSuccess(true);

            return result;

        } catch (Exception ex) {
            logger.warn("catch an exception when sending mssage:", ex);
        }

        return result;
    }


    @Override
    public void sendOneway(NatsmqMessage msg) {
        this.send(msg, new NatsmqSendCallback.DefaultNatsmqSendCallback(), 0);
    }

    @Override
    public void start() {
        try {
            logger.info("{} has been started", "NatsmqProducer");
        } catch (Exception ex) {
            logger.error("Catch an exception when closing connection! ", ex);
        }

    }

    @Override
    public void shutdown() {
        try {
            logger.info("{} has been closed", "NatsmqProducer");
        } catch (Exception ex) {
            logger.error("Catch an exception when closing connection! ", ex);
        }
    }
}
