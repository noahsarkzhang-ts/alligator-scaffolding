package org.noahsark.rpc.socket.thread;

import org.noahsark.rpc.common.thread.ServiceThread;
import org.noahsark.rpc.socket.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class SessionClearThread extends ServiceThread {

    private static Logger log = LoggerFactory.getLogger(SessionClearThread.class);

    /**
     * 会话超时时间
     */
    private static final int TIMEOUT = 3 * 60 * 1000;


    @Override
    public void run() {

        log.info("session clear thread start in {}", LocalDateTime.now());

        while (!this.isStopped()) {
            try {

                // 1. 清空 session
                SessionManager.getInstance().clear(TIMEOUT);

                TimeUnit.SECONDS.sleep(30);

            } catch (Exception ex) {
                log.warn("catch an exception!", ex);
            }
        }

        log.info(" {} stop in {}", this.getServiceName(), LocalDateTime.now());

    }

    @Override
    public String getServiceName() {
        return "session-clear-thread";
    }
}
