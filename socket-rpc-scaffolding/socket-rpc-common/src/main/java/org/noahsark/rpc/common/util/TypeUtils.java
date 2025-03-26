package org.noahsark.rpc.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class TypeUtils {

    /**
     * 获取泛型对象
     *
     * @param object 对象
     * @return 类型
     */
    public static Class<?> getFirstParameterizedType(Object object) {

        Class<?> thisClass = object.getClass();

        Type genericSuperType = thisClass.getGenericSuperclass();
        if (!(genericSuperType instanceof ParameterizedType)) {
            return Object.class;
        }

        int typeParamIndex = 0;

        Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();
        Type actualTypeParam = actualTypeParams[typeParamIndex];
        if (actualTypeParam instanceof ParameterizedType) {
            actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
        }

        if (actualTypeParam instanceof Class) {
            return (Class) actualTypeParam;
        }

        return Object.class;


    }
}
