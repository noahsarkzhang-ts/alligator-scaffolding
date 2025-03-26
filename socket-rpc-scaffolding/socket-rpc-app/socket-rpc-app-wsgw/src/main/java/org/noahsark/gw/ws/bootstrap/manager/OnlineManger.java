package org.noahsark.gw.ws.bootstrap.manager;

import org.apache.commons.lang3.StringUtils;
import org.noahsark.rpc.socket.session.Session;
import org.noahsark.rpc.socket.session.SessionManager;
import org.noahsark.rpc.socket.session.SessionStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在线用户管理
 *
 * @author zhangxt
 * @date 2024/03/20 09:44
 **/
public class OnlineManger {

    private static Logger log = LoggerFactory.getLogger(OnlineManger.class);

    private Map<Short, String> userDefaultClientMap = new HashMap<>();

    /**
     * 存放在线的会话:
     * key: 用户id_用户类型
     * value: map,key: 登陆类型，value: 会话
     */
    private Map<String, Map<String, Session>> sessionRepository = new ConcurrentHashMap<>();

    private static class OnlineMangerHolder {
        private static final OnlineManger INSTANCE = new OnlineManger();
    }

    private OnlineManger() {

        /**
         * 用户登陆类型：
         * 1：Android
         * 2: IOS
         * 3: Web
         */

        /**
         * 用户/设备默认登陆的类型：
         * 设备(1): 为 android
         * 管理员(2): 为 web
         * 用户（3）：为 web
         */
        userDefaultClientMap.put(Short.valueOf((short) 1), "1");
        userDefaultClientMap.put(Short.valueOf((short) 2), "3");
        userDefaultClientMap.put(Short.valueOf((short) 3), "3");
    }

    public static OnlineManger getInstance() {
        return OnlineMangerHolder.INSTANCE;
    }

    public List<Session> getSessions(String subjectId, short type) {

        List<Session> results = new ArrayList<>();

        if (subjectId == null) {
            return results;
        }

        String key = String.format("%s::%d", subjectId, type);
        Map<String, Session> sessions = sessionRepository.get(key);

        if (sessions == null || sessions.isEmpty()) {
            return results;
        }

        sessions.forEach((sessionKey, sessionValue) -> {
            results.add(sessionValue);
        });

        return results;

    }

    public Session getSession(String subjectId, short type) {
        if (subjectId == null) {
            return null;
        }

        String clientType = userDefaultClientMap.get(new Short(type));

        return getSession(subjectId, type, clientType);

    }

    public Session getSession(String subjectId, short type, String clientType) {
        if (subjectId == null) {
            return null;
        }

        String key = String.format("%s::%d", subjectId, type);
        Map<String, Session> sessions = sessionRepository.get(key);

        if (sessions == null || sessions.isEmpty()) {
            return null;
        }

        if (StringUtils.isEmpty(clientType)) {
            clientType = userDefaultClientMap.get(new Short(type));
        }

        return sessions.get(clientType);
    }

    public synchronized void putSession(String subjectId, short type, String clientType, Session session) {
        String key = String.format("%s::%d", subjectId, type);

        Map<String, Session> sessions = sessionRepository.get(key);

        if (sessions == null) {
            sessions = new HashMap<>();
            sessionRepository.put(key, sessions);
        }

        if (StringUtils.isEmpty(clientType)) {
            clientType = userDefaultClientMap.get(new Short(type));
        }

        sessions.put(clientType, session);

        // sessionMap.put(key, session);
        // copyOrBindSession(subjectId, type, session);
    }

    public synchronized void putSession(String subjectId, short type, Session session) {

        String clientType = userDefaultClientMap.get(new Short(type));
        putSession(subjectId, type, clientType, session);

        // copyOrBindSession(subjectId, type, session);
    }

    public synchronized Session checkOrRecoverSession(String subjectId, short type, Session newSession, String sessionId) {

        String clientType = userDefaultClientMap.get(new Short(type));

        return checkOrRecoverSession(subjectId, type, clientType, newSession, sessionId);
    }

    public synchronized Session checkOrRecoverSession(String subjectId, short type, String clientType, Session newSession, String sessionId) {

        if (StringUtils.isEmpty(clientType)) {
            clientType = userDefaultClientMap.get(new Short(type));
        }

        Session oldSession = getSession(subjectId, type, clientType);

        if (oldSession == null) {

            log.debug("Old session is null:{}/{}/{}", subjectId, type, clientType);

            putSession(subjectId, type, clientType, newSession);

            return newSession;
        }

        String currentSessionId = oldSession.getSessionId();
        log.info("Old sessionId:{}", currentSessionId);

        if (!currentSessionId.equals(sessionId)) {
            // 移除之前的会话
            log.info("Remove old session:{}", currentSessionId);

            SessionManager.getInstance().removeOfflineSession(oldSession);
            removeSession(subjectId, type, clientType);

            putSession(subjectId, type, clientType, newSession);

            return newSession;
        }

        if (!SessionStatusEnum.OFFLINE.equals(oldSession.getStatus())) {
            log.warn("Session status invalid:{}", currentSessionId);

            throw new IllegalStateException("Session status invalid");
        }

        log.info("Recovery old session:{}", currentSessionId);

        // 用新会话恢复旧的会话
        oldSession.recovery(newSession);

        // 移除新的会话
        SessionManager.getInstance().remove(newSession);
        SessionManager.getInstance().recovery(oldSession);

        return oldSession;
    }

    public synchronized void removeSession(String subjectId, short type) {
        String clientType = userDefaultClientMap.get(new Short(type));

        removeSession(subjectId, type, clientType);

    }

    public synchronized void removeSession(String sessionId, String subjectId, short type) {
        String clientType = userDefaultClientMap.get(new Short(type));

        removeSession(sessionId, subjectId, type, clientType);

    }

    public synchronized void removeSession(String sessionId, String subjectId, short type, String clientType) {
        log.info("Remove online session:{}/{}/{}/{}", sessionId, subjectId, type, clientType);

        String key = String.format("%s::%d", subjectId, type);

        Map<String, Session> sessions = sessionRepository.get(key);

        if (sessions == null) {
            return;
        }

        if (StringUtils.isEmpty(clientType)) {
            clientType = userDefaultClientMap.get(new Short(type));
        }

        if (StringUtils.isNotEmpty(sessionId)) {
            Session session = sessions.get(clientType);
            if (session != null) {
                String currentId = session.getSessionId();
                if (sessionId.equals(currentId)) {
                    log.info("Remove session:{}/{}", sessionId, clientType);

                    sessions.remove(clientType);
                }
            }
        } else {
            log.info("Remove session,clientType:{}", clientType);

            sessions.remove(clientType);
        }

        if (sessions.isEmpty()) {
            sessionRepository.remove(key);
        }

    }

    public synchronized void removeSession(String subjectId, short type, String clientType) {
        log.info("Remove online session:{}/{}/{}", subjectId, type, clientType);

        String key = String.format("%s::%d", subjectId, type);

        Map<String, Session> sessions = sessionRepository.get(key);

        if (sessions == null) {
            return;
        }

        if (StringUtils.isEmpty(clientType)) {
            clientType = userDefaultClientMap.get(new Short(type));
        }

        sessions.remove(clientType);

        if (sessions.isEmpty()) {
            sessionRepository.remove(key);
        }

    }

    public boolean isOnline(String subjectId, short type) {
        String clientType = userDefaultClientMap.get(new Short(type));

        return isOnline(subjectId, type, clientType);
    }

    public boolean isOnline(String subjectId, short type, String clientType) {
        if (StringUtils.isEmpty(clientType)) {
            clientType = userDefaultClientMap.get(new Short(type));
        }

        Session session = getSession(subjectId, type, clientType);

        if (session == null) {
            return false;
        }

        return session.isOnline();

    }

    public String getDefaultClientType(short type) {
        return userDefaultClientMap.get(new Short(type));
    }

}
