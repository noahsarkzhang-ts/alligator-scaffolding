package org.noahsark.rpc.mq;

import org.noahsark.rpc.common.remote.Response;

/**
 * MQ 多路响应类
 *
 * @author zhangxt
 * @date 2024/05/08 23:08
 **/
public class MqMultiResponse extends Response {

    /**
     * Topic
     */
    private String topic;

    public MqMultiResponse() {
    }

    public MqMultiResponse(Builder builder) {
        super(builder.responseBuilder);

        this.topic = builder.topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public static class Builder {
        private String topic;

        private Response.Builder responseBuilder = new Response.Builder();

        public Builder seqId(int seqId) {
            this.responseBuilder.seqId(seqId);
            return this;
        }

        public Builder cmd(int cmd) {
            this.responseBuilder.cmd(cmd);
            return this;
        }

        public Builder type(byte type) {
            this.responseBuilder.type(type);
            return this;
        }

        public Builder data(Object data) {
            this.responseBuilder.data(data);
            return this;
        }

        public Builder code(int code) {
            this.responseBuilder.code(code);
            return this;
        }

        public Builder msg(String msg) {
            this.responseBuilder.msg(msg);
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public MqMultiResponse build() {
            return new MqMultiResponse(this);
        }


    }
}
