package org.noahsark.rpc.common.remote;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class Request extends RpcCommand implements MultiEnable, ResponseBuilder, Serializable {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    protected transient int fanout = 1;

    public Request() {
    }

    public Request(Builder builder) {
        super(builder.commandBuilder);
    }

    public static final int nextId() {
        return NEXT_ID.getAndIncrement();
    }

    public void setRequestId(int i) {
    }

    @Override
    public Response build(int code, String msg) {

        Response response = new Response.Builder()
                .seqId(this.getSeqId())
                .cmd(this.getCmd())
                .type(RpcCommand.RESPONSE)
                .code(code)
                .msg(msg)
                .build();

        return response;
    }

    @Override
    public boolean multiEnable() {
        return false;
    }

    public static class Builder {
        private RpcCommand.Builder commandBuilder = new RpcCommand.Builder();

        public Builder seqId(int msgId) {
            this.commandBuilder.seqId(msgId);
            return this;
        }

        public Builder cmd(int cmd) {
            this.commandBuilder.cmd(cmd);
            return this;
        }

        public Builder type(byte type) {
            this.commandBuilder.type(type);
            return this;
        }

        public Builder data(Object data) {
            this.commandBuilder.data(data);
            return this;
        }

        public Request build() {

            return new Request(this);
        }
    }

    @Override
    public String toString() {
        return "Request{} " + super.toString();
    }

    public int getFanout() {
        return fanout;
    }

    public void setFanout(int fanout) {
        this.fanout = fanout;
    }

    public static Request build(RpcCommand command) {
        Request request = new Request.Builder()
                .seqId(command.getSeqId())
                .cmd(command.getCmd())
                .type(RpcCommand.REQUEST)
                .data(command.getData())
                .build();

        return request;
    }

}
