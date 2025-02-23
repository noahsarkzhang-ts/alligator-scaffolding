package org.noashark.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 版本工具类
 * @author zhangxt
 * @date 2024/04/14 13:08
 **/
public class VersionUtils {
    public static long versionNum2Long(String verNum) {
        int[] pn = {1 * 100 * 100 * 100, 1 * 100 * 100, 1 * 100, 1};

        long value = 0L;

        if (StringUtils.isBlank(verNum)) {
            return value;
        }

        String[] splits = verNum.split("\\.");
        if (splits.length > 4) {
            return value;
        }

        for (int i = 0; i < splits.length; i++) {
            value += Long.parseLong(splits[i]) * pn[i];
        }

        return value;

    }
}
