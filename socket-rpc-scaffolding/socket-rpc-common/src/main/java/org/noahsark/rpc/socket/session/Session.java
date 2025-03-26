package org.noahsark.rpc.socket.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import org.noahsark.rpc.common.remote.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class Session implements ChannelHolder {

    private static Logger log = LoggerFactory.getLogger(Session.class);

    private static final String SESSION_KEY_NAME = "NOAHSARK_SESSION";

    public static final AttributeKey<String> SESSION_KEY = AttributeKey.newInstance(SESSION_KEY_NAME);

    private String sessionId;

    private Subject subject;

    private Connection connection;

    private Date lastAccessTime;

    private ConnectionManager connectionManager;

    private SessionStatusEnum status;

    private boolean isRepeatLogin = false;

    /**
     * 根据 Connection 构造 会话
     *
     * @param connection 连接对象
     */
    public Session(Connection connection) {
        this.sessionId = UUID.randomUUID().toString();
        this.lastAccessTime = new Date();

        this.connection = connection;

        ConnectionManager connectionManager = new ConnectionManager();
        // TODO
        //connectionManager.setHeartbeatFactory(new WebsocketHeartbeatFactory());
        this.connectionManager = connectionManager;
        this.status = SessionStatusEnum.INIT;
    }

    @Override
    public void write(RpcCommand response) {
        this.connection.write(response);
    }

    /**
     * 远程方法调用（默认线程池）
     *
     * @param request         请求
     * @param commandCallback 回调
     * @param timeoutMillis   超时时间
     * @return RpcPromise
     */
    public RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis) {

        request.setSeqId(this.connection.nextId());
        RpcPromise promise = new RpcPromise();
        promise.invoke(this.connection, request, commandCallback, timeoutMillis);
        return promise;
    }

    /**
     * 远程方法调用（自定义线程池）
     *
     * @param request         请求
     * @param commandCallback 回调
     * @param timeoutMillis   超时时间
     * @param executor        响应的线程池
     * @return RpcPromise
     */
    public RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis,
                             EventExecutor executor) {

        request.setSeqId(this.connection.nextId());
        RpcPromise promise = new RpcPromise(executor);
        promise.invoke(this.connection, request, commandCallback, timeoutMillis);
        return promise;
    }

    /**
     * 同步方法调用
     *
     * @param request       请求
     * @param timeoutMillis 超时时间
     * @return 结果
     */
    public Object invokeSyc(Request request, int timeoutMillis) {

        request.setSeqId(this.connection.nextId());
        RpcPromise promise = new RpcPromise();

        return promise.invokeSync(this.connection, request, timeoutMillis);
    }

    public void invokeOneway(Request request) {
        this.connection.write(request);
    }

    @Override
    public PromiseHolder getPromiseHolder() {
        return connection;
    }

    /**
     * 根据 Channel 创建会话
     *
     * @param channel 通道
     * @return 会话
     */
    public static Session getOrCreatedSession(Channel channel) {

        String sessionId = channel.attr(SESSION_KEY).get();
        Session session;

        if (sessionId == null) {
            Connection connection = new Connection(channel);

            session = new Session(connection);
            channel.attr(SESSION_KEY).set(session.getSessionId());
            SessionManager.getInstance().addSession(session.getSessionId(), session);

            log.info("New Session:{}/{}", session.getSessionId(), channel);
        } else {
            session = SessionManager.getInstance().getSession(sessionId);
            // log.info("Reused Session:{}/{}", session.getSessionId(), channel);
        }

        return session;
    }

    /**
     * 根据连接创建会话
     *
     * @param connection 连接
     * @return 会话
     */
    public static Session getOrCreatedSession(Connection connection) {
        Channel channel = connection.getChannel();

        String sessionId = channel.attr(SESSION_KEY).get();
        Session session;

        if (sessionId == null) {
            session = new Session(connection);
            channel.attr(SESSION_KEY).set(session.getSessionId());
            SessionManager.getInstance().addSession(session.getSessionId(), session);

            log.info("New Session:{}/{}", session.getSessionId(), channel);
        } else {
            session = SessionManager.getInstance().getSession(sessionId);
            // log.info("Reused Session:{}/{}", session.getSessionId(), channel);
        }

        return session;
    }

    /**
     * 根据 Channel 获取会话
     *
     * @param channel 通道
     * @return 会话
     */
    public static Session getSession(Channel channel) {
        String sessionId = channel.attr(SESSION_KEY).get();
        return sessionId == null ? null : SessionManager.getInstance().getSession(sessionId);
    }

    @Override
    public Subject getSubject() {
        return subject;
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Channel getChannel() {
        return connection.getChannel();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setStatus(SessionStatusEnum status) {
        this.status = status;

        this.connection.setStatus(status);
    }

    public SessionStatusEnum getStatus() {
        return this.status;
    }

    public boolean isOnline() {
        return !SessionStatusEnum.LOGOUT.equals(this.status);
    }

    public void copyFrom(Session oldSession) {

        if (oldSession == null) {
            return;
        }
        // TOTO copy session data
        // 1. 复制 subject
        if (this.subject != null) {
            this.subject.copyFrom(oldSession.getSubject());
        }

        // 2. 复制缓存
        // TODO

    }

    public void recovery(Session newSession) {
        if (newSession == null) {
            return;
        }

        Channel oldChannel = this.getChannel();
        if (oldChannel != null) {
            oldChannel.attr(SESSION_KEY).set("");
            this.connection.resetChannel();

            log.info("Old channel:{}", oldChannel);

        }

        // 恢复连接
        Channel channel = newSession.getChannel();
        this.connection.resetChannel(channel);

        log.info("New channel:{}", channel);

        // 恢复绑定关系
        channel.attr(SESSION_KEY).set(this.sessionId);

        if (channel.equals(oldChannel)) {
            log.info("Old channel is same to new channel...");
            this.setRepeatLogin(true);
        }

    }

    public void processPendingMsgs() {
        this.connection.startToProcessPendingMsgs();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Session)) {
            return false;
        }
        Session session = (Session) obj;
        return getSessionId().equals(session.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId());
    }

    public boolean isRepeatLogin() {
        return isRepeatLogin;
    }

    public void setRepeatLogin(boolean repeatLogin) {
        isRepeatLogin = repeatLogin;
    }
}
