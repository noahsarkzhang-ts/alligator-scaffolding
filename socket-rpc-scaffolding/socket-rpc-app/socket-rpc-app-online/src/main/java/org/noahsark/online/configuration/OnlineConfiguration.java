package org.noahsark.online.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 在线模块配置类
 * @author zhangxt
 * @date 2024/05/11 20:44
 **/
@Data
@NoArgsConstructor
@Configuration
public class OnlineConfiguration {

    @Value("${mq.topic.rpc:zasafe.rpc.vssp}")
    private String rpcTopic;
}
