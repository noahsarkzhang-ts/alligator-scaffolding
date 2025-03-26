package org.noahsark.gw.ws.processor.login.dto;

import java.io.Serializable;

/**
 * 用户退出对象
 *
 * @author zhangxt
 * @date 2024/05/09 11:26
 **/
public class UserLogoutDTO implements Serializable {

    private Long tenantId;

    private Long customerId;

    private Long userId;

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

}
