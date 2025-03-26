package org.noahsark.online.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


/**
 * <p>
 * 用户登陆时长
 * </p>
 *
 * @author allen
 * @since 2024-12-10
 */
@Getter
@Setter
@TableName("omp_user_online_time")
public class UserOnlineTime implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id,使用雪花id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 用户类型,1:设备，2：管理员；3：用户
     */
    private Short subjectType;

    /**
     * 用户主体，sn/用户id
     */
    private String subjectId;

    /**
     * 登录的网关id
     */
    private String serverId;

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 登陆时间
     */
    private LocalDateTime loginTime;

    /**
     * 退出时间
     */
    private LocalDateTime logoutTime;

    /**
     * 登陆时长，单位为秒
     */
    private Long duration;

    /**
     * 用户登陆类型：
     * 1：Android
     * 2: IOS
     * 3: Web
     */
    private Short clientType;

}