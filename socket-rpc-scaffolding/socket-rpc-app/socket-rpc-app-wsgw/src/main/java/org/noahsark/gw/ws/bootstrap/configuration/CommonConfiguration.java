package org.noahsark.gw.ws.bootstrap.configuration;

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
public class CommonConfiguration {

    /**
     * 生成异步线程池
     * @return 异步线程池
     */
    @Bean("commonExecutor")
    public ThreadPoolTaskExecutor getAsyncTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setKeepAliveSeconds(120);
        taskExecutor.setThreadNamePrefix("commonExecutor-");

        return taskExecutor;

    }
}
