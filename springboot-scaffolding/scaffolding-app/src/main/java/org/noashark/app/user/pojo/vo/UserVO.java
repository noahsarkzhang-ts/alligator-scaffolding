package org.noashark.app.user.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserVO implements Serializable {

    /**
     * 主键，使用雪花id
     */
    @ApiModelProperty(value = "主键，使用雪花id")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long customerId;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Short gender;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String mobile;

    /**
     * email
     */
    @ApiModelProperty(value = "email")
    private String email;

    /**
     * 状态，1：正常；2：禁用；3：删除
     */
    @ApiModelProperty(value = "状态，1：正常；2：禁用；3：删除")
    private Short status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Long operatedBy;

}