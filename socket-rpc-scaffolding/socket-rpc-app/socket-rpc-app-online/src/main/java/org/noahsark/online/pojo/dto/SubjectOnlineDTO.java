package org.noahsark.online.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 用户在线
 * </p>
 *
 * @author allen
 * @since 2024-03-19
 */
@Getter
@Setter
public class SubjectOnlineDTO implements Serializable {

    /**
     * 主键,主键
     */
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
     * 登陆类型,1:设备，2：管理员；3：用户
     */
    private Short type;

    /**
     * 登录主体
     */
    private String subjectId;

    /**
     * 登录所在网关id
     */
    private String serverId;

    /**
     * 状态,1：在线,2：离线
     */
    private Short status;

    /**
     * 登陆时间
     */
    private LocalDateTime loginTime;

}