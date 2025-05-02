package org.noahsark.rpc.common.constant;

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
     * 用户登陆
     */
    public static final int CMD_USER_LOGIN = 104;


    /**
     * 用户退出
     */
    public static final int CMD_USER_LOGOUT = 105;


    /**
     * 用户上线事件
     */
    public static final int CMD_PUSH_LOGIN_CALL = 209;

    /**
     * 通知踢出用户
     */
    public static final int CMD_NOTIFY_KICK_OUT_CALL = 210;

    /**
     * 用户挤占下线事件
     */
    public static final int CMD_LOGOUT_OCCUPY = 404;

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
