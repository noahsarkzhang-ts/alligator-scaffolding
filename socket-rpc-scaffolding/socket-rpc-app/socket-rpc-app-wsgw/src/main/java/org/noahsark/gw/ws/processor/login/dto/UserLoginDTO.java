package org.noahsark.gw.ws.processor.login.dto;

import java.io.Serializable;

/**
 * 用户登陆对象
 *
 * @author zhangxt
 * @date 2024/05/09 11:11
 **/
public class UserLoginDTO implements Serializable {

    private Long tenantId;

    private Long customerId;

    private Long userId;

    /**
     * 用户登陆类型：
     * 1：Android
     * 2: IOS
     * 3: Web
     */
    private Short clientType;

    /**
     * jwt token
     */
    private String token;


    /**
     * 会话id
     */
    private String sessionId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Short getClientType() {
        return clientType;
    }

    public void setClientType(Short clientType) {
        this.clientType = clientType;
    }
}
