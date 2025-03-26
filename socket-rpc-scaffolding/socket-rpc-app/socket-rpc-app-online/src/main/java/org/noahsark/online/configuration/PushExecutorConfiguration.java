package org.noahsark.online.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
@Configuration
public class PushExecutorConfiguration {

    /**
     * 生成异步线程池
     *
     * @return 异步线程池
     */
    @Bean("pushExecutor")
    public ThreadPoolTaskExecutor getAsyncTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setKeepAliveSeconds(120);
        taskExecutor.setThreadNamePrefix("pushExecutor-");

        return taskExecutor;

    }
}
