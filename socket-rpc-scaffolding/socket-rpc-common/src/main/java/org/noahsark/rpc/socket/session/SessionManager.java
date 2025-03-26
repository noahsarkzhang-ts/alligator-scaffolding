package org.noahsark.rpc.socket.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.noahsark.rpc.common.remote.Subject;
import org.noahsark.rpc.socket.event.ClientDisconnectEvent;
import org.noahsark.rpc.socket.event.SessionOfflineEvent;
import org.noahsark.rpc.socket.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class SessionManager {

    private static Logger log = LoggerFactory.getLogger(SessionManager.class);

    private Map<String, Session> sessions = new HashMap<>();

    private Map<String, OfflineSessionWrapper> offlineMaps = new HashMap<>();

    private static class Holder {

        private static final SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void addSession(String key, Session value) {
        this.sessions.put(key, value);
    }

    public synchronized Session getSession(String key) {
        return (Session) this.sessions.get(key);
    }

    public synchronized Session remove(String key) {
        log.info("Remove session:{}", key);

        return (Session) this.sessions.remove(key);
    }

    public static boolean validate(Session session) {
        return null != session;
    }

    /**
     * 客户端断连处理
     *
     * @param ctx ctx
     */
    public synchronized void disconnect(ChannelHandlerContext ctx) {

        try {
            String sessionId = ctx.channel().attr(Session.SESSION_KEY).get();

            if (!StringUtil.isNullOrEmpty(sessionId)) {

                log.info("Client inactive:{}/{}", sessionId, ctx.channel());

                Session session = getSession(sessionId);

                if (session == null) {
                    log.info("Session is null:{}", ctx.channel());
                    return;
                }

                Subject subject = session.getSubject();
                EventBus.getInstance().post(new ClientDisconnectEvent(subject));

                SessionStatusEnum statusEnum = session.getStatus();
                if (SessionStatusEnum.ONLINE.equals(statusEnum)) {
                    // TODO offline 如果是异常退出，则保持会话
                    this.offline(session);
                } else {
                    remove(sessionId);
                }
            }

            ctx.channel().attr(Session.SESSION_KEY).set("");
            ctx.disconnect();
        } catch (Exception ex) {
            log.warn("Catch an exception when disconnecting channel.", ex);
        }
    }

    /**
     * 所有客户端断连处理
     */
    public void disconnectAll() {
        List<String> sessionKeys = new ArrayList<>(this.sessions.keySet());
        sessionKeys.forEach(key -> {
            Session session = getSession(key);
            Subject subject = session.getSubject();
            EventBus.getInstance().post(new ClientDisconnectEvent(subject));

            session.getChannel().attr(Session.SESSION_KEY).set("");
            remove(key);

            session.getChannel().disconnect();
        });
    }

    public synchronized void login(Session session) {
        session.setStatus(SessionStatusEnum.ONLINE);
    }

    public synchronized void logout(Session session) {
        session.setStatus(SessionStatusEnum.LOGOUT);

    }

    public synchronized void offline(Session session) {
        session.setStatus(SessionStatusEnum.OFFLINE);
        // 连接已经失效
        //session.setConnection(null);

        String sessionId = session.getSessionId();
        if (!offlineMaps.containsKey(sessionId)) {
            OfflineSessionWrapper sessionWrapper = new OfflineSessionWrapper(System.currentTimeMillis(), session);
            offlineMaps.put(sessionId, sessionWrapper);
        }

    }

    public synchronized void permanentOffline(Session session) {

        log.info("Close Session:{}", session.getSessionId());

        Subject subject = session.getSubject();
        EventBus.getInstance().post(new SessionOfflineEvent(subject));

        String sessionId = session.getSessionId();
        //this.sessions.remove(sessionId);
        this.remove(sessionId);

        this.offlineMaps.remove(sessionId);
    }

    public synchronized void removeOfflineSession(Session session) {
        String sessionId = session.getSessionId();

        this.remove(sessionId);
        this.offlineMaps.remove(sessionId);
    }

    public synchronized void remove(Session session) {
        String sessionId = session.getSessionId();

        this.remove(sessionId);
    }

    public synchronized void recovery(Session session) {
        String sessionId = session.getSessionId();

        this.offlineMaps.remove(sessionId);
    }

    /**
     * 清空过期的会话
     *
     * @param intervalMillis 超时时间
     */
    public void clear(int intervalMillis) {

        try {
            if (offlineMaps.isEmpty()) {
                return;
            }

            PriorityQueue<OfflineSessionWrapper> queue = new PriorityQueue<>();

            offlineMaps.forEach((key, sessionWrapper) -> {
                Session session = sessionWrapper.getSession();

                Subject subject = session.getSubject();

                if (subject != null) {
                    log.info("Offline user:{}", subject.getId());
                } else {
                    log.info("Offline user: null");
                }

                queue.offer(sessionWrapper);
            });

            OfflineSessionWrapper sessionWrapper;
            long timeStampMillis = 0L;
            long timeoutMillis = 0L;

            Instant instant = Instant.now();
            long currentMillis = instant.toEpochMilli();

            while (!queue.isEmpty()) {
                sessionWrapper = queue.peek();

                timeStampMillis = sessionWrapper.getTs();
                timeoutMillis = currentMillis - timeStampMillis;

                Session session = sessionWrapper.getSession();
                Subject subject = session.getSubject();
                String userId = "none";

                if (subject != null) {
                    userId = subject.getId();
                }

                if (timeoutMillis >= intervalMillis) {

                    this.permanentOffline(session);

                    queue.remove();

                    Connection connection = session.getConnection();
                    if (connection != null) {
                        connection.close();
                    }

                    // 清空用户登陆
                    session.setSubject(null);

                    log.warn("Session timeout: {},timeout: {}", userId, timeoutMillis);
                } else {
                    break;
                }

            }
        } catch (Exception ex) {
            log.warn("Catch an exception in clear session.", ex);
        }

    }

}
