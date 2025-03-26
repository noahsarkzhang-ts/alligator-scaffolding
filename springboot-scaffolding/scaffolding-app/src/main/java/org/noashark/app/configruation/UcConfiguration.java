package org.noashark.app.configruation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 统一通信配置类
 *
 * @author zhangxt
 * @date 2023/08/11 16:44
 **/
@Data
@NoArgsConstructor
@Configuration
public class UcConfiguration {

    @Value("${uc.iot.url}")
    private String iotUrl = "https://124.71.211.247:8887/";

    @Value("${uc.iot.username}")
    private String iotUsername;

    @Value("${uc.iot.password}")
    private String iotPwd;

    @Value("${uc.iot.defaultRuleChain:de9a3f70-1d73-11ef-82e7-9135bd6d073c}")
    private String iotDefaultRuleChain;

    @Value("${uc.iot.defaultDeviceProfile:45357e00-286b-11ef-a921-33a8fd2de73d}")
    private String iotDefaultDeviceProfile;

    @Value("${uc.iot.server.host}")
    private String iotServerHost;

    @Value("${uc.iot.server.mqtt-port}")
    private int iotServerMqttPort;

    @PostConstruct
    public void init() {
        iotUrl = !iotUrl.endsWith("/") ? iotUrl + "/" : iotUrl;
    }
}
