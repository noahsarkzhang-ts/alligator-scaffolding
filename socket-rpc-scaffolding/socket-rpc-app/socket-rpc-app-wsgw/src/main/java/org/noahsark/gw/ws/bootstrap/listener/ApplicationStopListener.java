package org.noahsark.gw.ws.bootstrap.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
@Component
public class ApplicationStopListener implements ApplicationListener<ContextClosedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStopListener.class);


    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        logger.info("Application stoped!");
    }
}
