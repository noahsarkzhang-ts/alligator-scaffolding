package org.noashark.common.constant;

/**
 * 错误码常量表
 *
 * @author zhangxt
 * @date 2024/04/02 16:25
 **/
public class ResultConstants {

    /******************************** 错误码 ************************************/
    // 成功code
    public static final int SUCCESS_CODE = 200;
    // 失败code
    public static final int FAIL_CODE = -1;

    // 超时
    public static final int TIMEOUT_CODE = 10001;
    // 异常
    public static final int REQUEST_EXCEPTION_CODE = 10002;
    // 参数不合法
    public static final int ILLEGAL_PARAMETER_CODE = 10003;


    /******************************** 提示信息 ************************************/
    // 成功消息
    public static final String SUCCESS_MSG = "Success";
    // 失败消息
    public static final String FAIL_MSG = "Fail";
    // 超时
    public static final String TIMEOUT_MSG = "request.timeout";
    // 请求异常
    public static final String REQUEST_EXCEPTION_MSG = "request.exception";
    // 参数不合法
    public static final String ILLEGAL_PARAMETER_MSG = "illegal.parameters";

}
