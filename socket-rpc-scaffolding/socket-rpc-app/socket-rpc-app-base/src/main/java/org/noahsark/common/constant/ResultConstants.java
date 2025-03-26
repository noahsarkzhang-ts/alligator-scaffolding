package org.noahsark.common.constant;

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
    // 设备不存在
    public static final int DEVICE_NOT_EXIST_CODE = 10100;
    // Token不合法
    public static final int TOKEN_ILLEGAL_CODE = 10102;
    // Token过期
    public static final int TOKEN_EXPIRES_CODE = 10103;
    // 设备未登陆
    public static final int DEVICE_NOT_LOGIN_CODE = 10104;

    // 用户不存在
    public static final int USER_NOT_EXIST_CODE = 10112;
    // 用户不在线
    public static final int USER_NOT_ONLINE_CODE = 10113;

    // 请求没有结果
    public static final int REQUEST_NO_RESULT_CODE = 10119;

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

    // 设备不存在
    public static final String DEVICE_NOT_EXIST_MSG = "device.not.exist";
    // 设备APP信息不存在
    public static final String DEVICE_APP_NOT_EXIST_MSG = "device.app.not.exist";
    // Token不合法
    public static final String TOKEN_ILLEGAL_MSG = "token.illegal";
    // Token过期
    public static final String TOKEN_EXPIRES_MSG = "token.expires";
    // 设备未登陆
    public static final String DEVICE_NOT_LOGIN_MSG = "device.not.login";

    // 用户不存在
    public static final String USER_NOT_EXIST_MSG = "user.not.exist";
    // 用户不存在
    public static final String USER_NOT_ONLINE_MSG = "user.not.inline";

    // 请求没有结果
    public static final String REQUEST_NO_RESULT_MSG = "request.no.result";

}
