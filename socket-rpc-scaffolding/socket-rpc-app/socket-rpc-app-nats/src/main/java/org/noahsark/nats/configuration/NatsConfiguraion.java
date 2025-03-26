package org.noahsark.nats.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Nats mq配置
 *
 * @author zhangxt
 * @date 2024/03/22 09:54
 **/
@Data
@NoArgsConstructor
@Configuration
public class NatsConfiguraion {

    @Value("${nats.enable}")
    private boolean enable;

    @Value("${nats.servers}")
    private String servers;
}
