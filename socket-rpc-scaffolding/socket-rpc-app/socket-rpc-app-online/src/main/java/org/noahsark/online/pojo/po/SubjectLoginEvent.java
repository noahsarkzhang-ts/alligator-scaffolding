package org.noahsark.online.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户登陆事件
 * </p>
 *
 * @author allen
 * @since 2024-05-27
 */
@Getter
@Setter
@TableName("omp_subject_login_event")
public class SubjectLoginEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,主键
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
     * 用户登陆类型：
     * 1：Android
     * 2: IOS
     * 3: Web
     */
    private Short clientType;

    /**
     * 用户主体，sn/用户id
     */
    private String subjectId;

    /**
     * 事件类型,1:上线，2：下线
     */
    private Short eventType;

    /**
     * 事件原因,1：正常上线/正常下线,2：超时下线；3：挤占下线
     */
    private Short eventCause;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

}