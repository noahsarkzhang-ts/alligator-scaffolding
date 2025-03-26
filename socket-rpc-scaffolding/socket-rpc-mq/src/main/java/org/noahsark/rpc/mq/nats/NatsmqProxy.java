package org.noahsark.rpc.mq.nats;

import org.noahsark.rpc.common.dispatcher.Dispatcher;
import org.noahsark.rpc.common.dispatcher.WorkQueue;
import org.noahsark.rpc.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * RabbitMQ 代理
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class NatsmqProxy extends AbstractMqProxy {

    private static Logger logger = LoggerFactory.getLogger(NatsmqProxy.class);

    private Properties config = new Properties();

    private NatsmqConsumer consumer;

    private NatsmqProducer producer;

    private List<NatsmqTopic> topics;

    private WorkQueue workQueue;

    private Dispatcher dispatcher;

    public NatsmqProxy(List<NatsmqTopic> topics, WorkQueue workQueue, Dispatcher dispatcher) {

        this.topics = topics;
        this.workQueue = workQueue;
        this.dispatcher = dispatcher;

        init();
    }

    private void init() {
        try {
            // 初始化 mq 生产者
            this.producer = new NatsmqProducer();

            // 初始化 mq 消息者
            this.consumer = new NatsmqConsumer(this.topics);
            // 注册监听器
            this.consumer.registerMessageListener(new DefaultmqMessageListener(this, this.workQueue, this.dispatcher));

            initHolder();

        } catch (Exception ex) {
            logger.error("Catch an exception when init rabbitmq proxy.", ex);
        }

    }

    @Override
    protected void initHolder() {
        promiseHolder = new MqPromiseHolder(producer);
        channelHolder = new MqChannelHolder(producer, promiseHolder);
    }

    @Override
    public void sendOneway(MqMultiRequest request) {
        request.setRequestId(promiseHolder.nextId());

        producer.sendOneway(producer.buildMessage(request));
    }

    @Override
    public void start() {
        producer.start();
        consumer.start();
    }

    @Override
    public void shutdown() {
        try {
            this.consumer.shutdown();
            this.producer.shutdown();
        } catch (Exception ex) {
            logger.error("Catch an exception when closing connection! ", ex);
        }
    }


}
