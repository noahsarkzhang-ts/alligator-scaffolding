package org.noahsark.gw.ws.service;

import org.noahsark.gw.ws.processor.login.dto.UserLoginDTO;
import org.noahsark.online.pojo.po.SubjectOnline;

/**
 * 用户登陆接口
 *
 * @author zhangxt
 * @date 2024/05/09 12:12
 **/
public interface IUserLoginService {

    void userLogin(UserLoginDTO userLoginInfo);

    void updateOnlineSubject(SubjectOnline subject);

}
