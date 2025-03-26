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
     * 推流
     */
    public static final int CMD_PUSH_STREAM = 200;

    /**
     * 一对一呼叫
     */
    public static final int CMD_CALL = 201;

    /**
     * 退出呼叫
     */
    public static final int CMD_LOGOUT_CALL = 202;

    /**
     * 关闭呼叫
     */
    public static final int CMD_CLOSE_CALL = 203;

    /**
     * 推送会话通道信息
     */
    public static final int CMD_PUSH_CHANNELS = 204;

    /**
     * 广播通道事件
     */
    public static final int CMD_BROADCAST_CHANNEL_EVENT = 205;

    /**
     * SOS呼叫
     */
    public static final int CMD_SOS_CALL = 206;

    /**
     * 推送用户退出会话事件
     */
    public static final int CMD_PUSH_LOGOUT_CALL = 207;

    /**
     * 挂断电话
     */
    public static final int CMD_HANG_UP_CALL = 208;

    /**
     * 用户上线事件
     */
    public static final int CMD_PUSH_LOGIN_CALL = 209;

    /**
     * 通知踢出用户
     */
    public static final int CMD_NOTIFY_KICK_OUT_CALL = 210;

    /**
     * 群组呼叫
     */
    public static final int CMD_MULTI_CALL = 300;

    /**
     * 邀请加入呼叫
     */
    public static final int CMD_INVITE_TO_JOIN_CALL = 301;

    /**
     * 邀请加入呼叫
     */
    public static final int CMD_PUSH_JOINED_CALL = 302;

    /**
     * 推送通道状态信息
     */
    public static final int CMD_PUSH_CHANNEL_STATUS = 303;

    /**
     * 拉人入会
     */
    public static final int CMD_PULL_IN_CALL = 304;

    /**
     * 通道控制命令
     */
    public static final int CMD_CHANNEL_CONTROL = 305;


    /**
     * 推送用户及通道信息
     */
    public static final int CMD_PUSH_USER_CHANNELS = 306;


    /**
     * 踢人
     */
    public static final int CMD_KICK_OUT_CALL = 307;

    /**
     * 群组消息
     */
    public static final int CMD_GROUP_MSG = 320;

    /**
     * 推送群组消息到客户端
     */
    public static final int CMD_PUSH_GROUP_MSG = 321;

    /**
     * 订阅群组消息
     */
    public static final int CMD_GROUP_MSG_SUBSCRIPTION = 322;

    /**
     * 推送群组未读消息到客户端
     */
    public static final int CMD_PUSH_GROUP_NONE_READ_MSG = 323;

    /**
     * 推送设备加入到的群组
     */
    public static final int CMD_PUSH_JOINED_GROUP_MSG = 324;

    /**
     * 增量推送设备加入到的群组
     */
    public static final int CMD_PUSH_INCREMENT_JOINED_GROUP_MSG = 325;

    /**
     * 多群组消息
     */
    public static final int CMD_MULTI_GROUP_MSG = 326;

    /**
     * 推送多群组消息到客户端
     */
    public static final int CMD_PUSH_MULTI_GROUP_MSG = 327;


    /**
     * 流关闭事件
     */
    public static final int CMD_STREAM_CLOSED = 400;

    /**
     * 无人观看事件
     */
    public static final int CMD_STREAM_NONE_READER = 401;

    /**
     * 用户上线/下线事件
     */
    public static final int CMD_USER_ONLINE = 402;

    /**
     * 消息已经送达事件
     */
    public static final int CMD_ARRIVED_MSG = 403;

    /**
     * 用户挤占下线事件
     */
    public static final int CMD_LOGOUT_OCCUPY = 404;

    /**
     * 推送设备配置命令
     */
    public static final int CMD_DEVICE_PUSH_CONFIG = 500;

    /**
     * 设备录像文件上传通知
     */
    public static final int CMD_DEVICE_RECORDING_UPLOAD = 501;

    /**
     * 推送GPS信息
     */
    public static final int CMD_PUSH_GPS = 502;

    /**
     * 推送电量信息
     */
    public static final int CMD_PUSH_POWER = 503;

    /**
     * 推送时序数据
     */
    public static final int CMD_PUSH_TELEMETRY_DATA = 504;

    /**
     * 推送广播消息
     */
    public static final int CMD_PUSH_MESSAGE_DATA = 505;

    /**
     * 设备取消激活通知
     */
    public static final int CMD_PUSH_DEVICE_DEACTIVATE = 506;

    /**
     * 推送企业下在线的设备GPS信息
     */
    public static final int CMD_CUSTOMER_PUSH_GPS = 507;

    /**
     * 推送增量电子围栏列表
     */
    public static final int CMD_PUSH_INCREMENT_FENCE = 508;

    /**
     * 推送增量考勤列表
     */
    public static final int CMD_PUSH_INCREMENT_CHECKING = 509;

    /**
     * 推送增量任务列表
     */
    public static final int CMD_PUSH_INCREMENT_TASK = 510;

    /**
     * 推送增量任务列表
     */
    public static final int CMD_SOS_EVENT = 511;


    /**
     * 未知命令
     */
    public static final int CMD_UNKNOWN = 10000;

}
