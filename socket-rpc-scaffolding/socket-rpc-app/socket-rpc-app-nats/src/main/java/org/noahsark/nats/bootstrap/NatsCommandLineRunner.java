package org.noahsark.nats.bootstrap;

import org.noahsark.nats.configuration.NatsConfiguraion;
import org.noahsark.rpc.mq.nats.manager.NatsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Nats mq 启动类
 *
 * @author zhangxt
 * @date 2024/03/22 09:55
 **/
@Component
public class NatsCommandLineRunner implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(NatsCommandLineRunner.class);

    @Autowired
    private NatsConfiguraion config;

    @Override
    public void run(String... args) throws Exception {

        try {

            if (!config.isEnable()) {
                log.info("Nats function is not turned on...");
                return;
            }

            NatsManager nm = NatsManager.getInstance();
            String servers = config.getServers();
            Properties properties = new Properties();
            properties.put("io.nats.client.servers", servers);

            log.info("Start to connect nats servers:{}", servers);

            // 建立连接
            nm.connect(properties);

        } catch (Exception ex) {

        }
    }
}
