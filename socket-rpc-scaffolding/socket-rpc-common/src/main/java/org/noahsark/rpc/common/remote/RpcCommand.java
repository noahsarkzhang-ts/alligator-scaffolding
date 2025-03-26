package org.noahsark.rpc.common.remote;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class RpcCommand implements Serializable {

    public static final byte REQUEST = (byte) 1;

    public static final byte RESPONSE = (byte) 2;

    public static final byte ONEWAY = (byte) 3;

    /**
     * 请求序号id
     */
    private int seqId;

    /**
     * 请求命令
     */
    private int cmd;

    /**
     * 消息类型，1：请求；2：响应；3：oneway
     */
    private byte type;

    /**
     * 消息内容
     */
    private Object data;


    public RpcCommand() {
    }

    /**
     * 构造 RpcCommand
     *
     * @param builder 构造器
     */
    public RpcCommand(Builder builder) {

        this.seqId = builder.seqId;
        this.cmd = builder.cmd;
        this.type = builder.type;
        this.data = builder.data;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 序列化 Json 数据
     *
     * @param json json
     * @return 命令对象
     */
    public static RpcCommand marshalFromJson(String json) {
        RpcCommand command = new RpcCommand();

        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        command.setSeqId(data.get("seqId").getAsInt());
        command.setCmd(data.get("cmd").getAsInt());
        command.setType(data.get("type").getAsByte());
        Object data1 = data.get("data");
        if (data1 != null) {
            command.setData(data1);
        }
        return command;
    }

    @Override
    public String toString() {
        return "RpcCommand{"
                + "seqId=" + seqId
                + ", type=" + type
                + ", cmd=" + cmd
                + ", data=" + data
                + '}';
    }

    public static class Builder {

        private int seqId;

        private int cmd;

        private byte type;

        private Object data;

        public Builder seqId(int msgId) {
            this.seqId = msgId;
            return this;
        }

        public Builder cmd(int cmd) {
            this.cmd = cmd;
            return this;
        }

        public Builder type(byte type) {
            this.type = type;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public RpcCommand build() {
            return new RpcCommand(this);
        }

    }
}
