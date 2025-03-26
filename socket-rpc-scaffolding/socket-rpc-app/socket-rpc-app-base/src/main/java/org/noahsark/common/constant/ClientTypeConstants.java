package org.noahsark.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆类型常量类
 *
 * @author zhangxt
 * @date 2024/09/11 12:46
 **/
public class ClientTypeConstants {

    private static Map<Short, Short> userDefaultClientMap = new HashMap<>();

    /**
     * 用户登陆类型: Android
     */
    public static final short CLIENT_TYPE_ANDROID = (short) 1;

    /**
     * 用户登陆类型: IOS
     */
    public static final short CLIENT_TYPE_IOS = (short) 2;

    /**
     * 用户登陆类型: Web
     */
    public static final short CLIENT_TYPE_WEB = (short) 3;

    static {
        /**
         * 用户登陆类型：
         * 1：Android
         * 2: IOS
         * 3: Web
         */

        /**
         * 用户/设备默认登陆的类型：
         * 设备(1): 为 android
         * 管理员(2): 为 web
         * 用户（3）：为 web
         */
        userDefaultClientMap.put(CommonConstants.SUBJECT_SN, CLIENT_TYPE_ANDROID);
        userDefaultClientMap.put(CommonConstants.SUBJECT_ADMIN, CLIENT_TYPE_WEB);
        userDefaultClientMap.put(CommonConstants.SUBJECT_USER, CLIENT_TYPE_WEB);
    }

    public static Short getDefaultClientType(Short subjectType) {
        return userDefaultClientMap.get(subjectType);
    }

    public static Short getDefaultClientType(Short clientType, Short subjectType) {
        if (!isEmpty(clientType)) {
            return clientType;
        }

        return userDefaultClientMap.get(subjectType);
    }

    public static boolean isEmpty(Short clientType) {
        return clientType == null || clientType == 0;
    }

}
