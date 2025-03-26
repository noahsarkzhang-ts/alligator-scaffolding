package org.noahsark.online.pojo.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户在线查询对象
 * @author zhangxt
 * @date 2024/05/12 12:21
 **/
@Getter
@Setter
public class SubjectOnlineQuery implements Serializable {

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


}
