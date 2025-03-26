package org.noahsark.rpc.mq.nats;

import org.noahsark.rpc.mq.Consumer;
import org.noahsark.rpc.mq.MessageListener;
import org.noahsark.rpc.mq.nats.manager.AbstractMqProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * RabbitMQ 消费者
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class NatsmqConsumer implements Consumer<NatsmqTopic> {

    private static Logger logger = LoggerFactory.getLogger(NatsmqConsumer.class);

    private MessageListener listener;

    private List<NatsmqTopic> topics;

    private List<AbstractMqProcessor> processors;

    public NatsmqConsumer() {
        this.topics = new ArrayList<>();
        this.processors = new ArrayList<>();
    }

    public NatsmqConsumer(List<NatsmqTopic> topics) {
        this.topics = topics;
        this.processors = new ArrayList<>();
    }

    @Override
    public void registerMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void subscribe(NatsmqTopic topic) {
        this.topics.add(topic);
    }

    @Override
    public void subscribe(List<NatsmqTopic> topics) {
        this.topics = topics;
    }

    @Override
    public void start() {

        try {
            topics.forEach(topic -> {
                RpcMqProcessor mqProcessor = new RpcMqProcessor(topic, this.listener);
                mqProcessor.register();
                processors.add(mqProcessor);
            });

            logger.info("{} has been started", "NatsmqConsumer");
        } catch (Exception ex) {
            logger.error("Catch an exception when starting NatsmqConsumer.", ex);
        }

    }

    @Override
    public void shutdown() {
        try {
            logger.info("{} has been closed", "NatsmqConsumer");
        } catch (Exception ex) {
            logger.error("Catch an exception when closing NatsmqConsumer.", ex);
        }
    }

    public static class RpcMqProcessor extends AbstractMqProcessor {

        private NatsmqTopic topic;

        private MessageListener listener;

        public RpcMqProcessor() {
        }

        public RpcMqProcessor(NatsmqTopic topic, MessageListener listener) {
            this.topic = topic;
            this.listener = listener;
        }

        @Override
        protected String getSubject() {
            return topic.getTopic();
        }

        @Override
        public void onMessage(String msg) {
            listener.consumeMessage(msg.getBytes(StandardCharsets.UTF_8));
        }
    }

}
