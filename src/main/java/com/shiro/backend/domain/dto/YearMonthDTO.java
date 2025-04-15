package com.shiro.backend.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "年月查询参数")
public class YearMonthDTO {

    @ApiModelProperty(value = "年份", required = true, example = "2025")
    @NotNull(message = "年份不能为空")
    private Integer year;

    @ApiModelProperty(value = "月份，可为空，为空时获取全年数据", required = false, example = "5")
    @Min(value = 1, message = "月份必须在1-12之间")
    @Max(value = 12, message = "月份必须在1-12之间")
    private Integer month;
}