package com.shiro.backend.domain.vo;

import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.enums.BillType;
import com.shiro.backend.enums.isDeletedEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 返回给前端的 查询账单 视觉对象
 */
@Data
@ApiModel(value = "查询用户被逻辑删除的账单-VO实体")
public class IsDeletedBillsVO {
    @ApiModelProperty(value = "账单ID，唯一约束，主键，由后端插入")
    private Long id;

    @ApiModelProperty(value = "金额，可以为正数或负数")
    private BigDecimal amount;

    @ApiModelProperty(value = "账单类型，收入或支出，使用整数表示")
    private BillType type;

    @ApiModelProperty(value = "交易分类ID，用来记录交易的类型")
    private Long categoryId;

    @ApiModelProperty(value = "消费详情，可为空，用于记录备注信息")
    private String detail;

    @ApiModelProperty(value = "账单日期，精确到年月日，由用户前端输入")
    private LocalDate date;

    @ApiModelProperty(value = "逻辑删除字段，标记是否被删除")
    private isDeletedEnum isDeleted;

    /**
     * 将查询到的数据封装为VO
     *
     * @param bills
     * @return
     */
    public static IsDeletedBillsVO toVO(Bills bills) {
        IsDeletedBillsVO vo = new IsDeletedBillsVO();
        vo.setId(bills.getId());
        vo.setAmount(bills.getAmount());
        vo.setType(bills.getType());
        vo.setCategoryId(bills.getCategoryId());
        vo.setDetail(bills.getDetail());
        vo.setDate(bills.getDate());
        vo.setIsDeleted(bills.getIsDeleted());
        return vo;
    }
}
