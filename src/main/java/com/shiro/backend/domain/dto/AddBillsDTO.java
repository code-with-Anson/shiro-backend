package com.shiro.backend.domain.dto;

import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.enums.BillType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "新增常规账单-数据传输对象")
public class AddBillsDTO {
    @ApiModelProperty(value = "金额", required = true, example = "52.52")
    private BigDecimal amount;

    @ApiModelProperty(value = "支出或收入", required = true, example = "支出")
    private BillType billType;

    @ApiModelProperty(value = "常规账单分类id", required = true, example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "账单备注", required = false, example = "爱丽丝很可爱")
    private String detail;

    @ApiModelProperty(value = "年月日时间", required = true, example = "2025-01-12")
    private LocalDate date;

    public Bills toEntity() {
        Bills bills = new Bills();
        bills.setAmount(this.amount);
        bills.setType(this.billType);
        bills.setCategoryId(this.categoryId);
        bills.setDetail(this.detail);
        bills.setDate(this.date);
        return bills;
    }
}
