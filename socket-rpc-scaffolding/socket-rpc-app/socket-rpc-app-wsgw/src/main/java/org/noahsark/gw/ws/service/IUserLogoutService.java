package org.noahsark.gw.ws.service;


import org.noahsark.rpc.socket.session.Session;

/**
 * 强制下线服务
 *
 * @author zhangxt
 * @date 2024/05/27 16:32
 **/
public interface IUserLogoutService {

    boolean checkAndForceLogout(Short subjectType, String subjectId, String sessionId);

    boolean checkAndForceLogout(Short subjectType, String subjectId, String clientType, String sessionId);

    void logout(Short subjectType, String subjectId, Session session, Short logoutType);

    void logout(Short subjectType, String subjectId, Session session, Short logoutType, String clientType);
}
