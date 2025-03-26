package org.noahsark.rpc.mq.nats.pojo;

/**
 * 单个响应结果
 * @author zhangxt
 * @date 2024/05/11 20:09
 **/
public class Result {

    /**
     * 返回码，成功为0
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;

    public Result() {
        this.code = 0;
        this.msg = "success";
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
