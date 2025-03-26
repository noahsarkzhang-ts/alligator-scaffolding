package org.noahsark.rpc.mq.nats.pojo;

import java.io.Serializable;
import java.util.Objects;

/**
 * 终端用户
 *
 * @author zhangxt
 * @date 2024/05/11 20:05
 **/
public class EndUser implements Serializable {

    /**
     * 终端用户id
     * 用户类型为1：sn
     * 用户类型为2：管理员id
     * 用户类型为3：用户id
     */
    private String userId;

    /**
     * 用户类型，1:sn,2:admin,3:user
     */
    private short type;

    /**
     * 用户登陆类型：
     * 1：Android
     * 2: IOS
     * 3: Web
     */
    private short clientType;

    public EndUser() {
    }

    public EndUser(String userId, short type) {
        this.userId = userId;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndUser)) return false;
        EndUser endUser = (EndUser) o;
        return type == endUser.type && clientType == endUser.clientType && Objects.equals(userId, endUser.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, type, clientType);
    }

    public short getClientType() {
        return clientType;
    }

    public void setClientType(short clientType) {
        this.clientType = clientType;
    }
}
