package org.noahsark.user.common.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "结果对象")
public class Response<T> implements Serializable {
    @ApiModelProperty(required = true, value = "响应码", example = "200")
    private Integer code;
    @ApiModelProperty(required = true, value = "消息信息", example = "success")
    private String msg;
    @ApiModelProperty(required = true, value = "结果")
    private T data;

    public Response() {
    }

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Response<?> fail(String msg) {

        return new Builder<>().code(-1).msg(msg).build();
    }

    public static <T> Response<T> ok(T data) {
        return new Builder<T>().code(200).msg("ok").data(data).build();
    }

    public static class Builder<T> {
        private Integer code;
        private String msg;
        private T data;

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Response<T> build() {
            return new Response<>(code, msg, data);
        }

    }

}
