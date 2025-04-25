package com.shiro.main.domain.dto;

import com.shiro.main.domain.po.RenewBill;
import com.shiro.main.enums.RenewType;
import com.shiro.main.enums.isDeletedEnum;
import com.shiro.main.enums.isRenew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "更新账单-数据传输对象", description = "用来传输更新循环账单的属性")
public class UpdateRenewBillDTO {
    @ApiModelProperty(value = "周期计费记录ID，主键，由后端生成", required = true)
    private Long id;

    @ApiModelProperty(value = "周期计费商品名称", required = false)
    private String name;

    @ApiModelProperty(value = "周期计费商品备注", required = false)
    private String details;

    @ApiModelProperty(value = "循环计费商品分类ID，用来记录对应的分类", required = false)
    private Long categoryId;

    @ApiModelProperty(value = "计费周期，数字含义由前端解析", required = false)
    private RenewType cycle;

    @ApiModelProperty(value = "循环付费开始时间", required = false)
    private LocalDate beginning;

    @ApiModelProperty(value = "循环付费结束时间", required = false)
    private LocalDate ending;

    @ApiModelProperty(value = "是否自动续费，数字含义由前端解析", required = false)
    private isRenew renew;

    @ApiModelProperty(value = "周期计费的费用", required = false)
    private BigDecimal cost;

    @ApiModelProperty(value = "逻辑删除字段，标记是否被删除", required = false)
    private isDeletedEnum isDeleted;

    public RenewBill toEntity() {
        RenewBill renewBill = new RenewBill();
        renewBill.setId(id);
        renewBill.setName(name);
        renewBill.setDetails(details);
        renewBill.setCategoryId(categoryId);
        renewBill.setCycle(cycle);
        renewBill.setBeginning(beginning);
        renewBill.setEnding(ending);
        renewBill.setRenew(renew);
        renewBill.setCost(cost);
        renewBill.setIsDeleted(isDeleted);
        return renewBill;
    }
}
