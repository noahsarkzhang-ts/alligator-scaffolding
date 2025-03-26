package org.noahsark.rpc.mq.nats;

import org.noahsark.rpc.mq.Topic;

/**
 * Nats TOPIC
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class NatsmqTopic implements Topic {

    /**
     * topic
     */
    private String topic;

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "NatsmqTopic{" +
                "topic='" + topic + '\'' +
                '}';
    }
}
