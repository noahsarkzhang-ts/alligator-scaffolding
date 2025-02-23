package org.noashark.common.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "分页查询返回对象")
@Getter
@Setter
public class PageVO<T> implements Serializable {

    /**
     * 当前页数
     */
    private Integer current;

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数")
    private Long totalRows;

    /**
     * 数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<T> records;
}
