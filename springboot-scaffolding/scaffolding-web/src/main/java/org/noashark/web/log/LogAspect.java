package org.noashark.web.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.noashark.web.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志切面类
 *
 * @author zhangxt
 * @date 2024/05/23 12:43
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Autowired
    private JacksonUtils jacksonUtils;

    /**
     * 以自定义 @Log 注解作为切面入口
     */
    @Pointcut("@annotation(org.noashark.web.log.Log)")
    public void Log() {
    }

    /*@Pointcut("execution(public *Controller.*(..))")
    public void LogV2() {
    }*/

    @Around("Log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        /**
         * 输出日志
         */
        log.info("URL: {}, Method: {}, Request Args: {}, Response Result  : {}, time: {}"
                , request.getRequestURL().toString(), request.getMethod()
                , jacksonUtils.toJson(joinPoint.getArgs())
                , jacksonUtils.toJson(result)
                , System.currentTimeMillis() - startTime);


        return result;
    }

}
