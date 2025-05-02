package org.noahsark.user.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "分页查询对象")
public class PageDTO<T> {

    @ApiModelProperty(required = true, value = "当前页码", example = "1")
    private Integer curPage;

    @ApiModelProperty(required = true, value = "每页记录数", example = "10")
    private Integer pageSize;

    private T query;
}
