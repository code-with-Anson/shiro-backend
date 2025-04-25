package com.shiro.main.domain.dto;

import com.shiro.main.domain.po.Bills;
import com.shiro.main.enums.BillType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "更新账单-数据传输对象", description = "用来传输更新常规账单的属性")
public class UpdateBillsDTO {
    @ApiModelProperty(value = "账单ID，唯一约束，主键，由后端插入", required = true, example = "1878773602640248834")
    private Long id;

    @ApiModelProperty(value = "金额，可以为正数或负数", required = false, example = "52.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "账单类型，收入或支出，使用整数表示", required = false, example = "收入")
    private BillType type;

    @ApiModelProperty(value = "交易分类ID，用来记录交易的类型", required = false, example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "消费详情，可为空，用于记录备注信息", required = false, example = "爱丽丝超可爱")
    private String detail;

    @ApiModelProperty(value = "账单日期，精确到年月日，由用户前端输入", required = false, example = "2025-03-06")
    private LocalDate date;

    public Bills toEntity() {
        Bills bill = new Bills();
        bill.setId(id);
        bill.setAmount(amount);
        bill.setType(type);
        bill.setCategoryId(categoryId);
        bill.setDetail(detail);
        bill.setDate(date);
        return bill;
    }
}
