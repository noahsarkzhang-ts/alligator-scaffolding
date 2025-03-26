package org.noahsark.online.pojo.vo;

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
public class SubjectOnlineVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 用户类型,1:设备，2：管理员；3：用户
     */
    private Short type;

    /**
     * 用户登陆类型：
     * 1：Android
     * 2: IOS
     * 3: Web
     */
    private Short clientType;

    /**
     * 登录主体
     */
    private String subjectId;

    /**
     * 登录所在网关id
     */
    private String serverId;

    /**
     * mq topic
     */
    private String topic;

    /**
     * 状态,1：在线,2：离线
     */
    private Short status;

    /**
     * 登陆时间
     */
    private LocalDateTime loginTime;

    /**
     * 是否有SOS呼叫权限，0：没有，1：有
     */
    private Short sosPerm;
}
