package org.noashark.web.log;


import java.lang.annotation.*;

/**
 * 日志类注解类
 *
 * @author zhangxt
 * @date 2024/05/23 12:41
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Log {

    /**
     * 日志描述信息
     *
     * @return
     */
    String description() default "";
}
