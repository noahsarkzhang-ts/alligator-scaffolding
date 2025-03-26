package org.noahsark.rpc.common.exception;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class RequestHandlerExcetion extends RuntimeException {
    public RequestHandlerExcetion() {
        super();
    }

    public RequestHandlerExcetion(String message) {
        super(message);
    }

    public RequestHandlerExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHandlerExcetion(Throwable cause) {
        super(cause);
    }

    protected RequestHandlerExcetion(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
