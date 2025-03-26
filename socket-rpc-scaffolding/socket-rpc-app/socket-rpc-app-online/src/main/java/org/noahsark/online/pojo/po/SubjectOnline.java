package org.noahsark.online.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
@TableName("omp_subject_online")
public class SubjectOnline implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Short TYPE_DEVICE = 1;

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
     * 登陆类型,1:设备，2：管理员；3：用户
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
