package org.noahsark.rpc.mq.nats;


import org.noahsark.rpc.mq.Message;

/**
 * RabbitMQ 消息
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class NatsmqMessage implements Message {

    private String topic;

    private byte[] content;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
