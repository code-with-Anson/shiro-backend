package com.shiro.main.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.shiro.main.enums.BillType;
import com.shiro.main.enums.isDeletedEnum;
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
@ApiModel(value = "账单记录实体类")
public class Bills implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账单ID，自增，主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @TableLogic
    @ApiModelProperty(value = "逻辑删除字段，标记是否被删除")
    private isDeletedEnum isDeleted;

    @ApiModelProperty(value = "记录创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "记录更新时间")
    private LocalDateTime updatedAt;

    // 添加分类名称字段，不对应数据库字段
    @ApiModelProperty(value = "账单分类名称，不对应数据库字段，用于联表查询后返回数据用")
    @TableField(exist = false)
    private String categoryName;
}
