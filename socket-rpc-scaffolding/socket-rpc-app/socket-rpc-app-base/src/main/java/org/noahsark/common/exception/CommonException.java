package org.noahsark.common.exception;

/**
 * 通用异常
 *
 * @author zhangxt
 * @date 2024/03/19 17:27
 **/
public class CommonException extends RuntimeException {

    private int code;

    private String message;

    public CommonException() {
        super();
    }

    public CommonException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CommonException(String message, int code, String message1) {
        super(message);
        this.code = code;
        this.message = message1;
    }

    public CommonException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public CommonException(Throwable cause, int code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
