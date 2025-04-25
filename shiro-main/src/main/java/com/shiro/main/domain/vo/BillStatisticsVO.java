package com.shiro.main.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "账单统计数据")
public class BillStatisticsVO {
    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "月份，全年统计时为null")
    private Integer month;

    @ApiModelProperty(value = "总支出金额")
    private BigDecimal totalExpense;

    @ApiModelProperty(value = "总收入金额")
    private BigDecimal totalIncome;

    @ApiModelProperty(value = "总净收入（收入-支出）")
    private BigDecimal netIncome;

    @ApiModelProperty(value = "账单总数量")
    private Integer billCount;

    @ApiModelProperty(value = "各分类金额明细")
    private Map<String, BigDecimal> categoryDetails;

    @ApiModelProperty(value = "支出分类明细")
    private Map<String, BigDecimal> expenseCategoryDetails;

    @ApiModelProperty(value = "收入分类明细")
    private Map<String, BigDecimal> incomeCategoryDetails;

    @ApiModelProperty(value = "月份统计明细，仅全年统计时返回")
    private List<MonthSummary> monthDetails;

    @Data
    public static class MonthSummary {
        @ApiModelProperty(value = "月份，1-12")
        private Integer month;

        @ApiModelProperty(value = "月度总支出")
        private BigDecimal expense;

        @ApiModelProperty(value = "月度总收入")
        private BigDecimal income;

        @ApiModelProperty(value = "月度净收入（收入-支出）")
        private BigDecimal netIncome;

        @ApiModelProperty(value = "月度账单数量")
        private Integer billCount;
    }
}