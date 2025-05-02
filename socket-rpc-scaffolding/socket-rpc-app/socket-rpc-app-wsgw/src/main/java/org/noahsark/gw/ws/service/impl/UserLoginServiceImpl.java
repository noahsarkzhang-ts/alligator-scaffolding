package org.noahsark.gw.ws.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.noahsark.common.constant.ClientTypeConstants;
import org.noahsark.common.constant.CommonConstants;
import org.noahsark.common.constant.ResultConstants;
import org.noahsark.common.exception.CommonException;
import org.noahsark.gw.ws.bootstrap.config.CommonConfig;
import org.noahsark.gw.ws.bootstrap.configuration.GwConfiguration;
import org.noahsark.gw.ws.bootstrap.manager.OnlineManger;
import org.noahsark.gw.ws.processor.login.dto.UserLoginDTO;
import org.noahsark.gw.ws.service.IUserLoginService;
import org.noahsark.online.pojo.po.SubjectLoginEvent;
import org.noahsark.online.pojo.po.SubjectOnline;
import org.noahsark.online.service.ISubjectLoginEventService;
import org.noahsark.online.service.ISubjectOnlineService;
import org.noahsark.user.pojo.po.User;
import org.noahsark.user.service.IUserService;
import org.noahsark.util.CollectionsUtils;
import org.noahsark.util.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户登陆实现类
 *
 * @author zhangxt
 * @date 2024/05/09 12:14
 **/
@Slf4j
@Service
    public class UserLoginServiceImpl implements IUserLoginService {

    @Autowired
    private IUserService userService;

    @Autowired
    private GwConfiguration gwConfiguration;

    @Autowired
    private ISubjectLoginEventService subjectLoginEventService;

    @Autowired
    private ISubjectOnlineService subjectOnlineService;

    @Autowired
    private CommonConfig config;

    @Override
    public void userLogin(UserLoginDTO userLoginInfo) {
        // 1. 用户是否存在
        Long tenantId = userLoginInfo.getTenantId();
        Long customerId = userLoginInfo.getCustomerId();
        Long userId = userLoginInfo.getUserId();

        Optional<User> userOpt = getUser(tenantId, customerId, userId);
        if (userOpt.isEmpty()) {
            log.info("User does not exist: {}/{}/{}", tenantId, customerId, userId);
            throw new CommonException(ResultConstants.USER_NOT_EXIST_CODE,
                    ResultConstants.USER_NOT_EXIST_MSG);
        }

        String appSecret = gwConfiguration.getJwtSecret();

        // 3. 检查token
        JwtValidator validator = new JwtValidator(appSecret, gwConfiguration.getJwtExpiresDays());
        DecodedJWT jwt;
        String token = userLoginInfo.getToken();
        try {
            jwt = validator.verify(token);
        } catch (Exception ex) {
            log.error("An exception occurred when verifying Token.", ex);
            throw new CommonException(ResultConstants.TOKEN_ILLEGAL_CODE,
                    ResultConstants.TOKEN_ILLEGAL_MSG);
        }

        // Token过期
        long exp = jwt.getClaim("exp").asLong();
        long currentTime = System.currentTimeMillis();
        log.info("exp:{},currentTime:{}", exp, currentTime);
        if (currentTime > exp * 1000) {
            log.info("Token expires: {}/{}", tenantId, customerId);
            throw new CommonException(ResultConstants.TOKEN_EXPIRES_CODE,
                    ResultConstants.TOKEN_EXPIRES_MSG);
        }

        // 3.2 记录用户上线事件
        SubjectLoginEvent loginEvent = new SubjectLoginEvent();

        loginEvent.setId(IdWorker.getId(loginEvent));
        loginEvent.setEventCause(CommonConstants.LOGIN_STATUS_ONLINE);
        loginEvent.setCustomerId(customerId);
        loginEvent.setTenantId(tenantId);
        loginEvent.setSubjectType(CommonConstants.SUBJECT_USER);
        loginEvent.setSubjectId(userId.toString());
        loginEvent.setEventType((short) 1);
        loginEvent.setEventTime(LocalDateTime.now());

        Short clientType = userLoginInfo.getClientType();
        if (ClientTypeConstants.isEmpty(clientType)) {
            clientType = Short.valueOf(OnlineManger.getInstance().getDefaultClientType((short) 3));
        }

        loginEvent.setClientType(clientType);

        subjectLoginEventService.save(loginEvent);

    }

    @Override
    public void updateOnlineSubject(SubjectOnline subject) {
        String subjectId = subject.getSubjectId();
        Short type = subject.getType();
        Short clientType = subject.getClientType();

        Optional<SubjectOnline> opt = subjectOnlineService.getOne(subjectId, type, clientType);

        if (opt.isPresent()) {
            Long id = opt.get().getId();

            SubjectOnline newSubject = new SubjectOnline();
            newSubject.setStatus(CommonConstants.LOGIN_STATUS_ONLINE);
            newSubject.setServerId(config.getServerId());
            newSubject.setTenantId(subject.getTenantId());
            newSubject.setCustomerId(subject.getCustomerId());
            newSubject.setLoginTime(LocalDateTime.now());

            UpdateWrapper<SubjectOnline> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);

            // 更新状态
            subjectOnlineService.update(newSubject, updateWrapper);
        } else {

            SubjectOnline newSubject = new SubjectOnline();
            newSubject.setId(IdWorker.getId(subject));

            newSubject.setTenantId(subject.getTenantId());
            newSubject.setCustomerId(subject.getCustomerId());

            newSubject.setStatus(CommonConstants.LOGIN_STATUS_ONLINE);
            newSubject.setLoginTime(LocalDateTime.now());

            newSubject.setServerId(config.getServerId());
            newSubject.setType(type);
            newSubject.setSubjectId(subjectId);
            newSubject.setClientType(clientType);

            // 插入新用户
            subjectOnlineService.save(newSubject);
        }
    }

    private Optional<User> getUser(Long tenantId, Long customerId, Long userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_id", tenantId);
        wrapper.eq("customer_id", customerId);
        wrapper.eq("id", userId);
        wrapper.eq("status", 1);

        List<User> list = userService.list(wrapper);

        if (!CollectionsUtils.isEmpty(list)) {
            return Optional.of(list.get(0));
        } else {
            return Optional.ofNullable(null);
        }

    }

}
