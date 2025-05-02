package org.noahsark.user.pojo.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户查询对象
 * @author zhangxt
 * @date 2025/02/23 19:02
 **/
@Getter
@Setter
public class UserQuery {

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
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long deptId;
}
