package org.noahsark.online.pojo.dto;

import java.util.List;
import java.util.Map;

/**
 * 多路用户结果
 *
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

    /**
     * 用户返回结果表
     */
    private Map<EndUser, Result> userResultMap;

    public MultiUserResults() {
        super();
    }

    public MultiUserResults(List<EndUser> successUsers, List<EndUser> failUsers, Map<EndUser, Result> userResultMap) {
        super();
        this.successUsers = successUsers;
        this.failUsers = failUsers;
        this.userResultMap = userResultMap;
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

    public Map<EndUser, Result> getUserResultMap() {
        return userResultMap;
    }

    public void setUserResultMap(Map<EndUser, Result> userResultMap) {
        this.userResultMap = userResultMap;
    }
}
