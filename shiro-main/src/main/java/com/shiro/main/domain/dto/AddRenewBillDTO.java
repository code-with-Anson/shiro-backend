package com.shiro.main.domain.dto;

import com.shiro.main.domain.po.RenewBill;
import com.shiro.main.enums.RenewType;
import com.shiro.main.enums.isRenew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "新增循环账单-数据传输对象")
public class AddRenewBillDTO {
    @ApiModelProperty(value = "周期计费商品名称", required = true, example = "循环账单name")
    private String name;

    @ApiModelProperty(value = "周期计费商品备注", required = false, example = "备注")
    private String details;

    @ApiModelProperty(value = "循环计费商品分类ID，用来记录对应的分类", required = true, example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "计费周期，数字含义由前端解析", required = true, example = "日付")
    private RenewType cycle;

    @ApiModelProperty(value = "循环付费开始时间", required = true, example = "2025-01-01")
    private LocalDate beginning;

    @ApiModelProperty(value = "循环付费结束时间", required = true, example = "2025-01-31")
    private LocalDate ending;

    @ApiModelProperty(value = "是否自动续费，数字含义由前端解析", required = true, example = "未开启")
    private isRenew renew;

    @ApiModelProperty(value = "周期计费的费用", required = true, example = "39.39")
    private BigDecimal cost;

    public RenewBill toEntity() {
        RenewBill renewBill = new RenewBill();
        renewBill.setName(name);
        renewBill.setDetails(details);
        renewBill.setCategoryId(categoryId);
        renewBill.setCycle(cycle);
        renewBill.setBeginning(beginning);
        renewBill.setEnding(ending);
        renewBill.setRenew(renew);
        renewBill.setCost(cost);
        return renewBill;
    }
}
