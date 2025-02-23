package org.noashark.util;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

/**
 * 集合工具类
 *
 * @author zhangxt
 * @date 2023/11/21 11:34
 **/
public class CollectionsUtils {

    /**
     * 判断集合对象是否为空
     *
     * @param collection 集合对象
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 交集
     *
     * @param set1 集合1
     * @param set2 集合1
     * @param <E>  类型
     * @return 结果
     */
    public static <E> Set<E> intersection(Set<E> set1, Set<E> set2) {
        Sets.SetView<E> set = Sets.intersection(set1, set2);

        return set;
    }

    /**
     * 差集
     *
     * @param set1 集合1
     * @param set2 集合1
     * @param <E>  类型
     * @return 结果
     */
    public static <E> Set<E> difference(Set<E> set1, Set<E> set2) {
        Sets.SetView<E> set = Sets.difference(set1, set2);

        return set;
    }

    /**
     * 并集
     *
     * @param set1 集合1
     * @param set2 集合1
     * @param <E>  类型
     * @return 结果
     */
    public static <E> Set<E> union(Set<E> set1, Set<E> set2) {
        Sets.SetView<E> set = Sets.union(set1, set2);

        return set;
    }

}
