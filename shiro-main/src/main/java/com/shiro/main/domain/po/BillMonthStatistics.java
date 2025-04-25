package com.shiro.main.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 账单月度统计表
 * </p>
 *
 * @author Anson
 * @since 2025-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bill_month_statistics")
@ApiModel(value = "BillMonthStatistics对象", description = "账单月度统计表")
public class BillMonthStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计记录ID，自增主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID，关联users表")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "统计年份")
    @TableField("year")
    private Integer year;

    @ApiModelProperty(value = "统计月份，1-12")
    @TableField("month")
    private Integer month;

    @ApiModelProperty(value = "月度总支出金额")
    @TableField("total_expense")
    private BigDecimal totalExpense;

    @ApiModelProperty(value = "月度总收入金额")
    @TableField("total_income")
    private BigDecimal totalIncome;

    @ApiModelProperty(value = "各分类金额明细，JSON格式存储")
    @TableField("category_details")
    private String categoryDetails;

    @ApiModelProperty(value = "支出分类明细，JSON格式")
    @TableField("expense_category_details")
    private String expenseCategoryDetails;

    @ApiModelProperty(value = "收入分类明细，JSON格式")
    @TableField("income_category_details")
    private String incomeCategoryDetails;

    @ApiModelProperty(value = "该月账单总数量")
    @TableField("bill_count")
    private Integer billCount;

    @ApiModelProperty(value = "记录创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "记录更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;


}
