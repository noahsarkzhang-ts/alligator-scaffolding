package org.noahsark.gw.ws.bootstrap.subject;

import org.noahsark.rpc.common.remote.Subject;

import java.util.Set;

/**
 * 用户对象
 *
 * @author zhangxt
 * @date 2024/03/08 15:52
 **/
public class UserSubject implements Subject {

    /**
     * sn/用户id/管理员id
     */
    private String subjectId;

    /**
     * 用户类型，1:sn, 2:管理员id, 3:用户id
     */
    private short type;

    /**
     * 用户登陆类型：
     * 1：Android
     * 2: IOS
     * 3: Web
     */
    private Short clientType;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 当前呼叫
     */
    private String callId;

    private Set<Long> groupIds;

    private boolean isRepeatLogin = false;

    private String sessionId;

    public UserSubject() {
    }

    public UserSubject(String subjectId, short type) {
        this.subjectId = subjectId;
        this.type = type;
    }

    public String getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return String.format("%s::%d", subjectId, type);
    }

    @Override
    public void copyFrom(Subject oldSubject) {
        if (oldSubject == null) {
            return;
        }

        UserSubject userSubject = (UserSubject) oldSubject;

        this.callId = userSubject.getCallId();
    }

    @Override
    public void setupRepeatLogin() {
        isRepeatLogin = true;
    }

    @Override
    public boolean isRepeatLogin() {
        return isRepeatLogin;
    }

    @Override
    public void resetRepeatLogin() {
        isRepeatLogin = false;
    }

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

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Set<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Set<Long> groupIds) {
        this.groupIds = groupIds;
    }

    public Short getClientType() {
        return clientType;
    }

    public void setClientType(Short clientType) {
        this.clientType = clientType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
