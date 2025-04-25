package com.shiro.main.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分页查询参数-数据传输对象")
public class PageDTO {

    @ApiModelProperty(value = "当前页", required = false, example = "1")
    private Long currentPage = 1L; // 默认当前页为1

    @ApiModelProperty(value = "默认分页大小", required = false, example = "5")
    private Long pageSize = 5L;   // 默认分页大小为5
}
