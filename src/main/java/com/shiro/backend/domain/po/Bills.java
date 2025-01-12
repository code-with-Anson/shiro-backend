package com.shiro.backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shiro.backend.enums.BillType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 账单记录表
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bills")
@ApiModel(value = "Bills对象", description = "账单记录表")
public class Bills implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账单ID，唯一约束，主键，由后端插入")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户ID，外键，关联user表的id")
    private Long userid;

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
    private Boolean isDeleted;

    @ApiModelProperty(value = "记录创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "记录更新时间")
    private LocalDateTime updatedAt;


}
