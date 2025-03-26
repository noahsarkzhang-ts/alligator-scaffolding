package org.noahsark.rpc.socket.session;

import org.noahsark.rpc.socket.heartbeat.HeartbeatFactory;
import org.noahsark.rpc.socket.heartbeat.HeartbeatStatus;
import org.noahsark.rpc.socket.heartbeat.RetryPolicy;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class ConnectionManager {

    private HeartbeatFactory<?> heartbeatFactory;

    private HeartbeatStatus heartbeatStatus;

    private RetryPolicy retryPolicy;

    public ConnectionManager() {
        heartbeatStatus = new HeartbeatStatus();
    }

    public HeartbeatFactory<?> getHeartbeatFactory() {
        return heartbeatFactory;
    }

    public void setHeartbeatFactory(HeartbeatFactory<?> heartbeatFactory) {
        this.heartbeatFactory = heartbeatFactory;
    }

    public HeartbeatStatus getHeartbeatStatus() {
        return heartbeatStatus;
    }

    public void setHeartbeatStatus(HeartbeatStatus heartbeatStatus) {
        this.heartbeatStatus = heartbeatStatus;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }
}
