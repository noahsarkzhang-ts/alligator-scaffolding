package org.noahsark.rpc.common.remote;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.noahsark.rpc.common.exception.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 多路RpcPromise实现
 *
 * @author zhangxt
 * @date 2024/05/08 09:59
 **/
public class MultiRpcPromise {
    private static Logger log = LoggerFactory.getLogger(MultiRpcPromise.class);

    private RpcModel model = RpcModel.ALL;

    /**
     * 请求id
     */
    private List<String> requestIds = new ArrayList<>();

    /**
     * 成功结果集
     * key: 请求id
     * value: 响应
     */
    private Map<String, Object> successResults = new HashMap<>();

    /**
     * 失败结果集
     * key: 请求id
     * value: 响应
     */
    private Map<String, Object> failResults = new HashMap<>();

    /**
     * 请求数
     */
    private int num = 1;

    /**
     * 当前响应结果数量
     */
    private int currentNum = 0;


    private int successNum = 0;

    /**
     * 失败的响应结果数量
     */
    private int failNum = 0;

    private RpcCommand command;

    /**
     * 锁
     */
    private CountDownLatch latch = new CountDownLatch(1);

    private CommandCallback callback;

    private int timeoutMillis = 0;

    private Timeout timeout;

    private boolean isFinished = false;

    public MultiRpcPromise() {
        startTimer();
    }

    public MultiRpcPromise(Builder builder) {
        this.model = builder.model;
        this.num = builder.num;
        this.callback = builder.callback;
        this.timeoutMillis = builder.timeoutMillis;

        startTimer();
    }

    public synchronized void addRequest(String requestId) {
        requestIds.add(requestId);
    }

    public synchronized MultiRpcPromise setSuccess(String requestId, Object result) {
        if (isFinished) {
            return this;
        }

        if (this.successResults.containsKey(requestId)) {
            return this;
        }

        this.successResults.put(requestId, result);
        currentNum++;
        successNum++;

        boolean isEnd = isFinish();
        if (isEnd) {
            latch.countDown();
            cancelTimer();
            isFinished = true;

            if (callback != null) {
                Map<String, Object> results = new HashMap<>();
                results.put("successResults", successResults);
                results.put("failResults", failResults);

                callback.callback(results, currentNum, num);
            }
        }

        return this;

    }

    public synchronized MultiRpcPromise setFailure(String requestId, Object result) {
        if (isFinished) {
            return this;
        }

        if (this.failResults.containsKey(requestId)) {
            return this;
        }

        this.failResults.put(requestId, result);
        currentNum++;
        failNum++;

        boolean isEnd = isFinish();
        if (isEnd) {
            latch.countDown();
            cancelTimer();
            isFinished = true;

            if (callback != null) {
                Map<String, Object> results = new HashMap<>();
                results.put("successResults", successResults);
                results.put("failResults", failResults);

                callback.callback(results, currentNum, num);
            }
        }

        return this;
    }

    public synchronized MultiRpcPromise setFailure(Exception exception) {

        log.warn("Catch an exception.", exception);

        if (isFinished) {
            return this;
        }

        latch.countDown();
        cancelTimer();
        isFinished = true;

        if (currentNum >= 1) {
            if (callback != null) {
                Map<String, Object> results = new HashMap<>();
                results.put("successResults", successResults);
                results.put("failResults", failResults);
                callback.callback(results, currentNum, num);
            }

            return this;
        }

        callback.failure(exception, currentNum, num);

        return this;

    }



    private Response buildResponse(int code, String msg, Object data) {
        return Response.buildResponse(this.command, data, code, msg);

    }

    public synchronized void end() {
        latch.countDown();
        this.cancelTimer();
    }

    public synchronized Map<String, Object> awaitAndGet() {

        try {

            boolean isEnd = isFinish();

            if (!isEnd) {
                latch.await();
            }

        } catch (Exception ex) {
        }

        Map<String, Object> results = new HashMap<>();
        results.put("success", successResults);
        results.put("fail", failResults);

        return results;
    }

    private boolean checkAtMostOnce() {
        if (currentNum >= 1 && successNum >= 1) {
            return true;
        }

        return checkAll();
    }

    private boolean checkAll() {
        if (currentNum == num) {
            return true;
        }

        return false;
    }

    private boolean isFinish() {
        boolean isEnd = false;

        switch (model) {
            case ALL:
                isEnd = checkAll();
                break;
            case AT_MOST_ONCE:
                isEnd = checkAtMostOnce();
                break;
            default:
                break;
        }

        return isEnd;
    }

    private void startTimer() {
        if (timeoutMillis > 0) {
            Timeout timeout = TimerHolder.getTimer().newTimeout(new TimerTask() {
                @Override
                public void run(Timeout timeout) throws Exception {
                    setFailure(new TimeoutException());
                }

            }, timeoutMillis, TimeUnit.MILLISECONDS);
            setTimeout(timeout);
        }
    }

    private void cancelTimer() {
        if (this.timeout != null) {
            this.timeout.cancel();
        }

        this.timeout = null;
    }

    private void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }


    public static enum RpcModel {
        AT_MOST_ONCE, ALL, AT_LEAST_ONCE
    }

    public static class Builder {
        private int num = 1;
        private RpcModel model = RpcModel.ALL;

        private CommandCallback callback;

        private int timeoutMillis = 0;

        public Builder() {

        }

        public Builder num(int num) {
            this.num = num;
            return this;
        }

        public Builder model(RpcModel model) {
            this.model = model;
            return this;
        }

        public Builder callback(CommandCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder timeoutMillis(int timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
            return this;
        }

        public MultiRpcPromise build() {
            return new MultiRpcPromise(this);
        }


    }
}
