package org.noahsark.rpc.socket.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import org.noahsark.rpc.common.exception.InvokeExcption;
import org.noahsark.rpc.common.remote.PromiseHolder;
import org.noahsark.rpc.common.remote.RpcCommand;
import org.noahsark.rpc.common.remote.RpcPromise;
import org.noahsark.rpc.common.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangxt
 * @desc: 连接对象
 * @version:
 * @date: 2021/7/21
 */
public class Connection implements PromiseHolder {

    private static Logger log = LoggerFactory.getLogger(Connection.class);

    private static final int MAX_QUEUE_NUM = 1000;

    private AtomicInteger nextId = new AtomicInteger(1);

    private Channel channel;

    private final ConcurrentHashMap<Integer, RpcPromise> futures = new ConcurrentHashMap<>(16);

    public static final AttributeKey<Connection> CONNECTION = AttributeKey.valueOf("connection");

    private BlockingQueue<RpcCommand> pendingQueue;

    private SessionStatusEnum status;

    /**
     * 构造函数
     */
    public Connection() {
        this.status = SessionStatusEnum.INIT;
    }

    /**
     * 构造函数
     *
     * @param channel 通道
     */
    public Connection(Channel channel) {
        this.channel = channel;

        this.channel.attr(CONNECTION).set(this);

        this.status = SessionStatusEnum.INIT;

        pendingQueue = new LinkedBlockingQueue<>(MAX_QUEUE_NUM);
    }

    public void resetChannel(Channel channel) {
        this.channel = channel;

        this.channel.attr(CONNECTION).set(this);
    }

    public void resetChannel() {
        this.channel.attr(CONNECTION).set(null);
    }

    @Override
    public int nextId() {
        return nextId.getAndIncrement();
    }


    @Override
    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    @Override
    public void registerPromise(Integer requestId, RpcPromise promise) {
        // log.info("register promise: {}", requestId);

        futures.put(requestId, promise);
    }

    @Override
    public RpcPromise removePromise(Integer requestId) {
        // log.info("remove an promise: {}， current size： {}", requestId, this.futures.size());

        RpcPromise promise = futures.get(requestId);

        if (promise.isRemoving()) {
            this.futures.remove(requestId);

        }

        return promise;
    }

    @Override
    public void removePromise(RpcPromise promise) {
        // log.info("remove promise: {}， current size： {}", promise.getRequestId(), this.futures.size());

        if (promise.isRemoving()) {
            this.futures.remove(promise.getRequestId());
        }
    }

    @Override
    public void write(RpcCommand command) {

        // 非离线状态直接发送消息
        if (!SessionStatusEnum.OFFLINE.equals(this.status)) {
            this.getChannel().writeAndFlush(command)
                    .addListener(new ChannelFutureListener() {

                        @Override
                        public void operationComplete(ChannelFuture cf) throws Exception {

                            RpcCommand localCommand = command;

                            if (!cf.isSuccess()) {

                                if (localCommand.getType() == 1) {
                                    RpcPromise promise = getPromise(localCommand.getSeqId());
                                    if (promise != null) {
                                        promise.setFailure(new InvokeExcption());
                                    }
                                }

                                log.error("Invoke send failed. The requestid is {} ", localCommand.getSeqId(),
                                        cf.cause());
                            }
                        }

                    });

            return;
        }

        // 离线状态则加入队列
        appendToPendingQueue(command);

    }

    public void processPendingMsgs() {

        if (this.pendingQueue.isEmpty()) {
            return;
        }

        int batchNum = 10;
        for (int index = 0; index < batchNum; index++) {
            RpcCommand command = this.pendingQueue.poll();

            if (command != null) {
                this.getChannel().writeAndFlush(command)
                        .addListener(new ChannelFutureListener() {

                            @Override
                            public void operationComplete(ChannelFuture cf) throws Exception {
                                RpcCommand localCommand = command;

                                if (!cf.isSuccess()) {

                                    if (localCommand.getType() == 1) {
                                        RpcPromise promise = getPromise(localCommand.getSeqId());
                                        if (promise != null) {
                                            promise.setFailure(new InvokeExcption());
                                        }
                                    }

                                    log.error("Invoke send failed. The requestid is {} ", localCommand.getSeqId(),
                                            cf.cause());
                                }
                            }

                        });
            } else {
                break;
            }
        }

        if (!this.pendingQueue.isEmpty()) {
            this.channel.eventLoop().schedule(this::processPendingMsgs, 5, TimeUnit.SECONDS);
        }
    }

    public void startToProcessPendingMsgs() {

        if (!this.channel.isActive()) {
            log.warn("Channel is not active...");

            return;
        }

        if (this.pendingQueue.isEmpty()) {

            log.info("No pending msg need to process.");

            return;
        }

        log.info("Need to process the pending msg:{}", this.pendingQueue.size());

        this.channel.eventLoop().schedule(this::processPendingMsgs, 5, TimeUnit.SECONDS);
    }

    private void appendToPendingQueue(RpcCommand command) {

        boolean success = this.pendingQueue.offer(command);

        if (!success) {
            log.warn("Pending msg Queue is full,msg is discarded:{}", JsonUtils.toJson(command));
        } else {
            log.info("The msg is added to the pending queue:{}", JsonUtils.toJson(command));
        }
    }

    public Channel getChannel() {
        return channel;
    }

    /**
     * 设置通道
     *
     * @param channel 通道
     */
    public void setChannel(Channel channel) {
        this.channel = channel;

        this.channel.attr(CONNECTION).set(this);
    }

    /**
     * 关闭通道
     */
    public void close() {
        if (channel != null) {

            try {
                log.info("Close channel:{}", channel);

                this.channel.attr(Session.SESSION_KEY).set("");
                this.channel.close();

            } catch (Exception ex) {
                log.warn("Catch an exception when closing channel.", ex);
            }
        }
    }

    public SessionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SessionStatusEnum status) {
        this.status = status;
    }

    public boolean isEmptyOfPendingQueue() {
        return this.pendingQueue.isEmpty();
    }
}
