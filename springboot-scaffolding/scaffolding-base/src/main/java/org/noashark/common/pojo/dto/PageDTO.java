package org.noashark.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ApiModel(value = "分页查询对象")
public class PageDTO<T> {

    @Range(min = 1, max = 100000, message = "请输入正确的页码")
    @ApiModelProperty(required = true, value = "当前页码", example = "1")
    private Integer curPage;

    @Range(min = 1, max = 1000, message = "请输入正确的记录数")
    @ApiModelProperty(required = true, value = "每页记录数", example = "10")
    private Integer pageSize;

    private T query;
}
