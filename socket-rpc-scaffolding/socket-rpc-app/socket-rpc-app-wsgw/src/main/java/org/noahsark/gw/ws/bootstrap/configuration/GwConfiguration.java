package org.noahsark.gw.ws.bootstrap.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * GW 配置类
 * @author zhangxt
 * @date 2024/05/10 20:47
 **/
@Data
@NoArgsConstructor
@Configuration
public class GwConfiguration {

    @Value("${vssp.jwt.secret}")
    private String jwtSecret;

    @Value("${vssp.jwt.expires-days}")
    private int jwtExpiresDays;

    @Value("${mq.topic.userOnline}")
    private String userOnlineTopic;

    @Value("${mq.topic.arrivedMsg}")
    private String arrivedMsgTopic;

    @Value("${vssp.device.auth:true}")
    private Boolean deviceAuth;

}
