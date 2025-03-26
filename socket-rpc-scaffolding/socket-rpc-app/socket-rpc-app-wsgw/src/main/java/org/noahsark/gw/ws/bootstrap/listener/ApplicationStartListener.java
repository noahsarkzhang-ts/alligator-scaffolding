package org.noahsark.gw.ws.bootstrap.listener;

import org.noahsark.gw.ws.bootstrap.config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);

    @Autowired
    private CommonConfig config;


    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        logger.info("Application start!");
    }
}
