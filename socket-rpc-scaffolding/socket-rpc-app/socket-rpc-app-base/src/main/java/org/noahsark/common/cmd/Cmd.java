package org.noahsark.common.cmd;

/**
 * 命令常量类
 *
 * @author zhangxt
 * @date 2024/03/21 18:38
 **/
public class Cmd {

    /**
     * 设备登陆
     */
    public static final int CMD_PING_PONG = 10;

    /**
     * MQ 指令
     */
    public static final int CMD_MQ_DOWNWARD = 20;

    /**
     * 设备登陆
     */
    public static final int CMD_DEVICE_LOGIN = 100;

    /**
     * 设备退出
     */
    public static final int CMD_DEVICE_LOGOUT = 101;

    /**
     * 管理员登陆
     */
    public static final int CMD_ADMIN_LOGIN = 102;

    /**
     * 管理员退出
     */
    public static final int CMD_ADMIN_LOGOUT = 103;


    /**
     * 用户登陆
     */
    public static final int CMD_USER_LOGIN = 104;


    /**
     * 用户退出
     */
    public static final int CMD_USER_LOGOUT = 105;

    /**
     * 推送GPS信息
     */
    public static final int CMD_PUSH_GPS = 502;

    /**
     * 推送电量信息
     */
    public static final int CMD_PUSH_POWER = 503;

    /**
     * 未知命令
     */
    public static final int CMD_UNKNOWN = 10000;

}
