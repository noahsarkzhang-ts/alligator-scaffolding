package org.noahsark.rpc.mq;

import org.noahsark.rpc.common.remote.Request;
import org.noahsark.rpc.common.remote.Response;
import org.noahsark.rpc.common.remote.RpcCommand;

import java.io.Serializable;
import java.util.List;

/**
 * MQ请求
 *
 * @author zhangxt
 * @date 2024/05/07 15:48
 **/
public class MqMultiRequest extends Request {

    /**
     * 目标用户
     */
    private List<EndUser> targets;

    /**
     * Topic
     */
    private String topic;

    /**
     * 响应 Topic
     */
    private String repliedTopic;

    public MqMultiRequest() {
    }

    @Override
    public Response build(int code, String msg) {

        Response response = new MqMultiResponse.Builder()
                .seqId(this.getSeqId())
                .cmd(this.getCmd())
                .type(RpcCommand.RESPONSE)
                .code(code)
                .topic(repliedTopic)
                .msg(msg)
                .build();

        return response;
    }

    @Override
    public boolean multiEnable() {
        return true;
    }

    public MqMultiRequest(Builder builder) {
        super(builder.requestBuilder);

        this.targets = builder.targets;
        this.topic = builder.topic;
        this.repliedTopic = builder.repliedTopic;

        setFanout(this.targets.size());
    }

    public List<EndUser> getTargets() {
        return targets;
    }

    public void setTargets(List<EndUser> targets) {
        this.targets = targets;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getRepliedTopic() {
        return repliedTopic;
    }

    public void setRepliedTopic(String repliedTopic) {
        this.repliedTopic = repliedTopic;
    }

    public static class Builder {
        private Request.Builder requestBuilder = new Request.Builder();

        private List<EndUser> targets;

        private String topic;

        private String repliedTopic;

        public Builder seqId(int msgId) {
            this.requestBuilder.seqId(msgId);
            return this;
        }

        public Builder cmd(int cmd) {
            this.requestBuilder.cmd(cmd);
            return this;
        }

        public Builder type(byte type) {
            this.requestBuilder.type(type);
            return this;
        }

        public Builder data(Object data) {
            this.requestBuilder.data(data);
            return this;
        }

        public Builder targets(List<EndUser> targets) {
            this.targets = targets;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;

            return this;
        }

        public Builder repliedTopic(String repliedTopic) {
            this.repliedTopic = repliedTopic;

            return this;
        }

        public MqMultiRequest build() {

            return new MqMultiRequest(this);
        }
    }

    public static class EndUser implements Serializable {

        /**
         * 终端用户id
         * 用户类型为1：sn
         * 用户类型为2：管理员id
         * 用户类型为3：用户id
         */
        private String userId;

        /**
         * 用户类型，1:sn,2:admin,3:user
         */
        private short type;

        /**
         * 用户登陆类型：
         * 1：Android
         * 2: IOS
         * 3: Web
         */
        private Short clientType;

        public EndUser() {
        }

        public EndUser(String userId, short type) {
            this.userId = userId;
            this.type = type;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public short getType() {
            return type;
        }

        public void setType(short type) {
            this.type = type;
        }

        public Short getClientType() {
            return clientType;
        }

        public void setClientType(Short clientType) {
            this.clientType = clientType;
        }

        @Override
        public String toString() {
            return "EndUser{" +
                    "userId='" + userId + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

}
