package org.noahsark.common.constant;

/**
 * 通用常量类
 *
 * @author zhangxt
 * @date 2024/03/20 09:52
 **/
public class CommonConstants {

    /**
     * 设备登陆类型
     */
    public static final short SUBJECT_SN = (short) 1;

    /**
     * 管理员登陆类型
     */
    public static final short SUBJECT_ADMIN = (short) 2;

    /**
     * 用户登陆类型
     */
    public static final short SUBJECT_USER = (short) 3;

    /**
     * 在线登陆状态
     */
    public static final short LOGIN_STATUS_ONLINE = (short) 1;

    /**
     * 离线登陆状态
     */
    public static final short LOGIN_STATUS_OFFLINE = (short) 2;

    /**
     * 流状态，1:初始态
     */
    public static final short STREAM_STATUS_INIT = (short) 1;

    /**
     * 流状态，2:开通中
     */
    public static final short STREAM_STATUS_APPLAY = (short) 2;

    /**
     * 流状态，3:推流中
     */
    public static final short STREAM_STATUS_PUSHING = (short) 3;

    /**
     * 流状态，4:推流结束
     */
    public static final short STREAM_STATUS_END = (short) 4;

    /**
     * 通道类型，1:音频
     */
    public static final short CHANNEL_TYPE_AUDIO = (short) 1;

    /**
     * 通道类型，1:视频
     */
    public static final short CHANNEL_TYPE_VIDEO = (short) 2;

    /**
     * 逻辑删除状态，与id值相同为删除
     */
    public static final String LOGIC_DELETE_VALUE = "id";

    /**
     * 逻辑删除状态,1为未删除
     */
    public static final String LOGIC_NOT_DELETE_VALUE = "1";

    /**
     * JWT token 有效期
     */
    public static final int EXPIRATION_DAYS = 7;

    /**
     * MQ 调用超时时间
     */
    public static final int RPC_TIMEOUT_MILLIS = 120 * 1000;

    /**
     * 能力类型:IOT
     */
    public static final String CAPA_IOT = "IOT";

    /**
     * 能力类型:SIP
     */
    public static final String CAPA_SIP = "SIP";

    /**
     * 能力类型:GB28181
     */
    public static final String CAPA_GB28181 = "GB28181";

    /**
     * 能力类型:ZAGC
     */
    public static final String CAPA_ZAGC = "ZAGC";

    /**
     * 下线类型：正常下线
     */
    public static final short LOGOUT_NORMAL = 1;

    /**
     * 下线类型：连接断线
     */
    public static final short LOGOUT_UNCONNECTED = 2;

    /**
     * 下线类型：挤占下线
     */
    public static final short LOGOUT_OCCUPY = 3;

    /**
     * 网关类型：1，平台
     */
    public static final short GW_VSSP = 1;

    /**
     * 网关类型：2，低端安全帽
     */
    public static final short GW_RD = 2;

    /**
     * 用户登陆类型,1：Android
     */
    public static final short CLIENT_TYPE_ANDROID = 1;

    /**
     * 用户登陆类型,2: IOS
     */
    public static final short CLIENT_TYPE_IOS = 2;

    /**
     * 用户登陆类型,3: Web
     */
    public static final short CLIENT_TYPE_WEB = 3;

    /**
     * 消息烦类型，1：发给设备
     */
    public static final short MSG_TYPE_DEVICE = 1;

    /**
     * 消息烦类型，2：发给群组
     */
    public static final short MSG_TYPE_GROUP = 2;

    /**
     * 消息烦类型，3：发给群组标签
     */
    public static final short MSG_TYPE_GROUP_LABEL = 3;

    /**
     * 消息烦类型，4：发给设备虚拟群组
     */
    public static final short MSG_TYPE_VIRTUAL_GROUP = 4;


}
