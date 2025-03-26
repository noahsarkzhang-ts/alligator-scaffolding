package org.noahsark.rpc.socket.ws.pojo;

/**
 * Token信息
 * @author zhangxt
 * @date 2025/03/22 19:56
 **/
public class TokenInfo {

    private String userName;

    private String token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
