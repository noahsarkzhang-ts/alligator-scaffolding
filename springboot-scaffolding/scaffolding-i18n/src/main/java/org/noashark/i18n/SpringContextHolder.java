package org.noashark.i18n;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.function.SingletonSupplier;

import java.util.function.Supplier;

/**
 * spring上下文工具类
 *
 * @author zhangxt
 * @date 2024/04/28 16:04
 **/
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public SpringContextHolder() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static <T> Supplier<T> getSupplier(Class<T> clazz) {
        return SingletonSupplier.of(() -> {
            return applicationContext.getBean(clazz);
        });
    }

    public static <T> Supplier<T> getSupplier(Class<T> clazz, String beanName) {
        return SingletonSupplier.of(() -> {
            T object = applicationContext.getBean(beanName, clazz);

            return object;
        });
    }
}
