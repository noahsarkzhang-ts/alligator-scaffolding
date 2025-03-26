package org.noahsark.rpc.mq.nats.pojo;

import java.util.List;

/**
 * 多路用户结果
 * @author zhangxt
 * @date 2024/05/11 22:33
 **/
public class MultiUserResults extends Result {

    /**
     * 成功的用户列表
     */
    private List<EndUser> successUsers;

    /**
     * 失败的用户列表
     */
    private List<EndUser> failUsers;

    public MultiUserResults() {
        super();
    }

    public MultiUserResults(List<EndUser> successUsers, List<EndUser> failUsers) {
        super();
        this.successUsers = successUsers;
        this.failUsers = failUsers;
    }

    public List<EndUser> getSuccessUsers() {
        return successUsers;
    }

    public void setSuccessUsers(List<EndUser> successUsers) {
        this.successUsers = successUsers;
    }

    public List<EndUser> getFailUsers() {
        return failUsers;
    }

    public void setFailUsers(List<EndUser> failUsers) {
        this.failUsers = failUsers;
    }
}
