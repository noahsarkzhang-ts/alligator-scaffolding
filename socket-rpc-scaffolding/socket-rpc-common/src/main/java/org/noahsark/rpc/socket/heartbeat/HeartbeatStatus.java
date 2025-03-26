package org.noahsark.rpc.socket.heartbeat;

import java.time.Instant;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class HeartbeatStatus {

    public static final int TIMEOUT_PING_COUNT = 3;

    public static int TIMEOUT_SECOND = 45;

    private int count;

    private long lastReceiveTime;

    public HeartbeatStatus() {
        this.count = 0;
        this.lastReceiveTime = Instant.now().getEpochSecond();
    }

    public synchronized void increment() {
        count++;
    }

    public synchronized void reset() {
        count = 0;
    }

    /**
     * 自增并判断是否超时
     * @return boolean
     */
    public synchronized boolean incAndTimeout() {

        count++;

        if (count >= 3) {
            return true;
        }

        return false;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getLastReceiveTime() {
        return lastReceiveTime;
    }

    public void setLastReceiveTime(long lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }

    @Override
    public String toString() {
        return "HeartbeatStatus{"
                + "count="
                + count
                + ", lastReceiveTime="
                + lastReceiveTime
                + '}';
    }
}
