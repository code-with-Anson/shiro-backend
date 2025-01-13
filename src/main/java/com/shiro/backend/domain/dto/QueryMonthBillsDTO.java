package com.shiro.backend.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "按月份查询账单-数据传输对象")
public class QueryMonthBillsDTO {
    @ApiModelProperty(value = "要查询的年份", required = true, example = "2025")
    private int year;
    @ApiModelProperty(value = "要查询的月份", required = true, example = "1")
    private int month;
}
