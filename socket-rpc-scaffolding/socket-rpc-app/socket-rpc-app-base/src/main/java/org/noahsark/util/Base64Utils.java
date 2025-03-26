package org.noahsark.util;

import java.util.Base64;

/**
 * Base64工具类
 *
 * @author zhangxt
 * @date 2024/12/12 17:41
 **/
public class Base64Utils {

    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

}
