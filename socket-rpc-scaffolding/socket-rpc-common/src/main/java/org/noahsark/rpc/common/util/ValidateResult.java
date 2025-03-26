package org.noahsark.rpc.common.util;

/**
 * 校验结果
 */
public class ValidateResult {
    // 是否成功
    private boolean success;
    // 失败原因
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}