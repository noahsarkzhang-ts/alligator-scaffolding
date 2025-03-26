package org.noahsark.rpc.socket.session;

import org.noahsark.rpc.common.exception.TimeoutException;
import org.noahsark.rpc.common.remote.RpcPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @author: zhangxt
 * @desc: Future管理类
 * @version:
 * @date: 2021/7/21
 */
public class FutureManager {

    private static Logger log = LoggerFactory.getLogger(FutureManager.class);

    private Map<Integer, RpcPromise> futures = new HashMap<>();
    private PriorityQueue<RpcPromise> queue = new PriorityQueue<>();

    private static class FutureManagerHolder {
        private static final FutureManager instance = new FutureManager();
    }

    private FutureManager() {
    }

    public static FutureManager getInstance() {
        return FutureManagerHolder.instance;
    }

    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    public void registerPromise(Integer requestId, RpcPromise promise) {
        futures.put(requestId, promise);
        queue.add(promise);
    }

    public void removePromis(Integer requestId) {
        this.queue.remove(this.futures.get(requestId));
        this.futures.remove(requestId);
    }

    public void removePromis(RpcPromise promise) {
        queue.remove(promise);
        this.futures.remove(promise.getRequestId());
    }

    /**
     * 清除过期的对象
     *
     * @param intervalMillis 过期时长
     */
    public void clear(int intervalMillis) {

        Instant instant = Instant.now();
        long currentMillis = instant.toEpochMilli();

        RpcPromise promise = null;
        long timeStampMillis = 0L;
        long timeoutMillis = 0L;

        while (!queue.isEmpty()) {
            promise = queue.peek();

            timeStampMillis = promise.getTimeStampMillis();
            timeoutMillis = currentMillis - timeStampMillis;

            if (timeoutMillis >= intervalMillis) {

                promise.setFailure(new TimeoutException());
                this.removePromis(promise);

                log.warn("Request timeout: {},timeout: {}", promise.getRequestId(), timeoutMillis);
            } else {
                break;
            }
        }

    }

}
