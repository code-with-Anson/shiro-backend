package com.shiro.backend.domain.vo;

import com.shiro.backend.domain.po.RenewBill;
import com.shiro.backend.enums.RenewType;
import com.shiro.backend.enums.isDeletedEnum;
import com.shiro.backend.enums.isRenew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 返回给前端的 查询账单 视觉对象
 */
@Data
@ApiModel(value = "查询所有循环账单-VO实体")
public class QueryRenewBillVO implements Serializable {
    @ApiModelProperty(value = "周期计费记录ID，主键，由后端生成")
    private Long id;

    @ApiModelProperty(value = "循环账单关联的用户id")
    private Long userId;

    @ApiModelProperty(value = "周期计费商品名称")
    private String name;

    @ApiModelProperty(value = "周期计费商品备注")
    private String details;

    @ApiModelProperty(value = "循环计费商品分类ID，用来记录对应的分类")
    private Long categoryId;

    @ApiModelProperty(value = "计费周期，数字含义由前端解析")
    private RenewType cycle;

    @ApiModelProperty(value = "循环付费开始时间")
    private LocalDate beginning;

    @ApiModelProperty(value = "循环付费结束时间")
    private LocalDate ending;

    @ApiModelProperty(value = "是否自动续费，数字含义由前端解析")
    private isRenew renew;

    @ApiModelProperty(value = "周期计费的费用")
    private BigDecimal cost;

    @ApiModelProperty(value = "逻辑删除字段，标记是否被删除")
    private isDeletedEnum isDeleted;


    /**
     * 将查询到的数据封装为VO
     *
     * @param renewBill
     * @return
     */
    public static QueryRenewBillVO toVO(RenewBill renewBill) {
        QueryRenewBillVO vo = new QueryRenewBillVO();
        vo.setId(renewBill.getId());
        vo.setUserId(renewBill.getUserId());
        vo.setName(renewBill.getName());
        vo.setDetails(renewBill.getDetails());
        vo.setCategoryId(renewBill.getCategoryId());
        vo.setCycle(renewBill.getCycle());
        vo.setBeginning(renewBill.getBeginning());
        vo.setEnding(renewBill.getEnding());
        vo.setRenew(renewBill.getRenew());
        vo.setCost(renewBill.getCost());
        vo.setIsDeleted(renewBill.getIsDeleted());
        return vo;
    }
}
