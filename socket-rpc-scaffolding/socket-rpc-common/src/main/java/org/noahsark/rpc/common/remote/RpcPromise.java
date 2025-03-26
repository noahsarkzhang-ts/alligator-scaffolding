package org.noahsark.rpc.common.remote;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.noahsark.rpc.common.exception.InvokeExcption;
import org.noahsark.rpc.common.exception.TimeoutException;
import org.noahsark.rpc.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class RpcPromise extends DefaultPromise<Object> implements Comparable<RpcPromise> {

    private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

    private static final UnorderedThreadPoolEventExecutor EVENT_EXECUTOR = new UnorderedThreadPoolEventExecutor(
            20);

    private static final ScheduledThreadPoolExecutor TIME_OUT_EXECUTOR = new ScheduledThreadPoolExecutor(2);

    private long timeStampMillis;

    private int requestId;

    private int fanout = 1;

    private AtomicInteger currentFanout = new AtomicInteger(0);

    private boolean isFailure = false;

    private Timeout timeout;

    private List<Object> results;

    /**
     * 构造函数
     */
    public RpcPromise() {
        super(EVENT_EXECUTOR);
        Instant instant = Instant.now();
        timeStampMillis = instant.toEpochMilli();
    }

    /**
     * 构造函数
     */
    public RpcPromise(EventExecutor executor) {
        super(executor);
        Instant instant = Instant.now();
        timeStampMillis = instant.toEpochMilli();
    }

    @Override
    public Promise<Object> setSuccess(Object result) {

        if (this.fanout == 1) {
            return super.setSuccess(result);
        }

        if (this.currentFanout.get() <= this.fanout) {
            synchronized (this) {

                this.currentFanout.incrementAndGet();

                if (results == null) {
                    results = new ArrayList<>();
                }

                this.results.add(result);

                if (this.currentFanout.get() == this.fanout) {
                    return super.setSuccess(results);
                }
            }
        }

        return this;
    }

    @Override
    public Promise<Object> setFailure(Throwable cause) {

        if (this.fanout == 1) {
            isFailure = true;
            return super.setFailure(cause);
        }

        synchronized (this) {
            if (results == null) {
                results = new ArrayList<>();
            }

            isFailure = true;
            return super.setSuccess(results);
        }

    }

    /**
     * 添加回调函数
     *
     * @param promiseHolder 容器
     * @param request       请求
     * @param callback      回调函数
     */
    public void addCallback(PromiseHolder promiseHolder, Request request, CommandCallback callback) {
        this.addListener(future -> {

            Object result;

            RpcPromise promise = (RpcPromise) future;

            try {
                result = future.get();

                callback.callback(result, currentFanout.get(), fanout);

            } catch (Throwable ex) {
                callback.failure(ex, currentFanout.get(), fanout);
                log.warn("catch an exception.", ex);
            } finally {
                promise.cancelTimeout();
                promiseHolder.removePromise(request.getSeqId());
            }
        });
    }

    /**
     * 同步调用
     *
     * @param promiseHolder 调用的持有者
     * @param request       请求
     * @param timeoutMillis 超时时间
     * @return 结果
     */
    public Object invokeSync(PromiseHolder promiseHolder, Request request, int timeoutMillis) {

        this.invoke(promiseHolder, request, null, timeoutMillis);

        try {
            return this.get();

        } catch (Exception ex) {
            log.warn("catch an exception. ", ex);
        } finally {
            this.cancelTimeout();
            promiseHolder.removePromise(request.getSeqId());
        }

        return null;
    }

    /**
     * 远程方法调用
     *
     * @param promiseHolder   调用的持有者
     * @param request         请求
     * @param commandCallback 回调函数
     * @param timeoutMillis   超时时间
     * @param retries         重试次数
     */
    public void invoke(PromiseHolder promiseHolder, Request request, CommandCallback commandCallback,
                       int timeoutMillis, int retries) {

        //log.debug("Send cmd : {} / {}", JsonUtils.toJson(request), retries);

        promiseHolder.registerPromise(request.getSeqId(), this);
        if (commandCallback != null) {
            addCallback(promiseHolder, request, commandCallback);
        }

        final int nextRetries = retries - 1;

        this.requestId = request.getSeqId();
        this.fanout = request.getFanout();

        try {
            //add timeout
            if (timeoutMillis > 0) {
                Timeout timeout = TimerHolder.getTimer().newTimeout(new TimerTask() {
                    @Override
                    public void run(Timeout timeout) throws Exception {
                        RpcPromise promise = promiseHolder.removePromise(request.getSeqId());

                        log.info("Request timeout:{}", JsonUtils.toJson(request));

                        // 默认重试三次
                        if (nextRetries > 0) {
                            TIME_OUT_EXECUTOR.schedule(() -> {
                                log.info("Execute timeout task:{}", JsonUtils.toJson(request));

                                request.setSeqId(promiseHolder.nextId());
                                RpcPromise newPromise = new RpcPromise();
                                newPromise.invoke(promiseHolder, request, commandCallback, timeoutMillis, nextRetries);

                                //invoke(promiseHolder, request, commandCallback, timeoutMillis, nextRetries);

                            }, 15, TimeUnit.SECONDS);
                        } else {
                            if (promise != null) {
                                promise.setFailure(new TimeoutException());
                            }
                        }
                    }

                }, timeoutMillis, TimeUnit.MILLISECONDS);

                setTimeout(timeout);
            }

            promiseHolder.write(request);

        } catch (Exception ex) {
            log.error("Exception caught when sending invocation. The requestId is {}", request.getSeqId(), ex);

            RpcPromise promise = promiseHolder.removePromise(request.getSeqId());
            if (promise != null) {
                promise.setFailure(new InvokeExcption());
            }
        }
    }

    /**
     * 远程方法调用
     *
     * @param promiseHolder   调用的持有者
     * @param request         请求
     * @param commandCallback 回调函数
     * @param timeoutMillis   超时时间
     */
    public void invoke(PromiseHolder promiseHolder, Request request, CommandCallback commandCallback,
                       int timeoutMillis) {

        // 默认重试3次
        invoke(promiseHolder, request, commandCallback, timeoutMillis, 4);
    }

    public boolean isRemoving() {

        return isFailure || this.fanout == 1 || this.currentFanout.get() >= fanout;
    }


    public long getTimeStampMillis() {
        return timeStampMillis;
    }

    public void setTimeStampMillis(long timeStampMillis) {
        this.timeStampMillis = timeStampMillis;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }

    /**
     * 取消定时器
     */
    public void cancelTimeout() {
        if (this.timeout != null) {
            this.timeout.cancel();

            this.timeout = null;
        }
    }

    public static void main(String[] args) {

    }

    @Override
    public int compareTo(RpcPromise promise) {
        return (int) (this.timeStampMillis - promise.getTimeStampMillis());
    }
}
