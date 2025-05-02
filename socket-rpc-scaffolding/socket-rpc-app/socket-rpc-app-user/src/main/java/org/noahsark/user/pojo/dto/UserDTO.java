package org.noahsark.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDTO implements Serializable {

    /**
     * 主键，使用雪花id
     */
    @ApiModelProperty(value = "主键，使用雪花id")
    private Long id;

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
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

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

}