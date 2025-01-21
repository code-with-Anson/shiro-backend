package com.shiro.backend.domain.dto;

import com.shiro.backend.enums.BillType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 这个DTO是通过联表查询得到的
 * 通过自定SQL进行联表查询，得到Bills实体类，同时将查到的分类名称封装到这个新的DTO类里面
 * 这样就可以把这个DTO一整个传给VO，让VO执行封装方法返回给前端
 */
@Data
@ApiModel(value = "在Bills实体类的基础上加入分类名称-传输实体类")
public class TransBillswithCategoryNameDTO {
    @ApiModelProperty(value = "账单ID，唯一约束，主键，由后端插入", required = true, example = "1878773602640248834")
    private Long id;

    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "金额，可以为正数或负数", required = false, example = "52.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "账单类型，收入或支出，使用整数表示", required = false, example = "收入")
    private BillType type;

    @ApiModelProperty(value = "交易分类ID，用来记录交易的类型", required = false, example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "交易分类名称，用来记录交易的类型", required = false, example = "汐落")
    private Long categoryName;


    @ApiModelProperty(value = "消费详情，可为空，用于记录备注信息", required = false, example = "爱丽丝超可爱")
    private String detail;

    @ApiModelProperty(value = "账单日期，精确到年月日，由用户前端输入", required = false, example = "2025-03-06")
    private LocalDate date;
}
