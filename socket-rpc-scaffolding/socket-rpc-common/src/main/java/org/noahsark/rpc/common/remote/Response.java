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
public class Response extends RpcCommand implements Serializable {

    public static final int SUCCESS = 0;

    public static final String SUCCESS_MESSAGE = "success";

    public static final int FAIL = -1;

    public static final String FAIL_MESSAGE = "fail";

    private int code;

    private String msg;

    public Response() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 构建 Response
     *
     * @param builder 构造器
     */
    public Response(Builder builder) {

        super(builder.commandBuilder);

        this.code = builder.code;
        this.msg = builder.msg;
    }

    public static class Builder {

        private int code;

        private String msg;

        private RpcCommand.Builder commandBuilder = new RpcCommand.Builder();

        public Builder seqId(int seqId) {
            this.commandBuilder.seqId(seqId);
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

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }


    /**
     * 构建通用响应
     *
     * @param request 请求
     * @param code    编码
     * @param message 消息
     * @return 响应
     */
    public static Response buildCommonResponse(RpcCommand request, int code, String message) {

        Response command = new Builder()
                .seqId(request.getSeqId())
                .cmd(request.getCmd())
                .type(RpcCommand.RESPONSE)
                .code(code)
                .msg(message)
                .build();

        return command;
    }

    /**
     * 构建响应结果
     *
     * @param request 请求
     * @param data    数据
     * @param code    编码
     * @param message 消息
     * @return 响应
     */
    public static Response buildResponse(RpcCommand request, Object data, int code, String message) {

        Response command = new Builder()
                .seqId(request.getSeqId())
                .cmd(request.getCmd())
                .type(RpcCommand.RESPONSE)
                .code(code)
                .msg(message)
                .data(data)
                .build();

        return command;
    }

    /**
     * 根据 resutlt 构造响应结果
     *
     * @param request 请求
     * @param result  result
     * @return 结果
     */
    public static Response buildResponseFromResult(RpcCommand request, Object result) {

        Response command = new Builder()
                .seqId(request.getSeqId())
                .cmd(request.getCmd())
                .type(RpcCommand.RESPONSE)
                .data(result)
                .build();

        return command;
    }

    /**
     * 序列化 Json 数据
     *
     * @param json    json
     * @param command command
     * @return 命令对象
     */
    public static Response marshalFromJson(String json, RpcCommand command) {
        Response response = new Response();

        response.setSeqId(command.getSeqId());
        response.setCmd(command.getCmd());
        response.setType(command.getType());
        response.setData(command.getData());

        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        if (data == null) {
            return response;
        }
        response.setCode(data.get("code").getAsInt());
        response.setMsg(data.get("msg").getAsString());

        Object responseData = data.get("data");
        if (responseData != null) {
            command.setData(responseData);
        }
        return response;
    }


    public static Response defaultResponse(RpcCommand command) {
        return Response.buildResponse(command, null, SUCCESS, SUCCESS_MESSAGE);
    }
}
