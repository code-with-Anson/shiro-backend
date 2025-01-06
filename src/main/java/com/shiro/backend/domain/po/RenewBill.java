package com.shiro.backend.domain.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 周期计费表，存储循环计费信息
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("renew_bill")
@ApiModel(value="RenewBill对象", description="周期计费表，存储循环计费信息")
public class RenewBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "周期计费记录ID，主键，由后端生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "周期计费商品名称")
    private String name;

    @ApiModelProperty(value = "周期计费商品备注")
    private String details;

    @ApiModelProperty(value = "计费周期，数字含义由前端解析")
    private Integer cycle;

    @ApiModelProperty(value = "循环付费开始时间")
    private LocalDate beginning;

    @ApiModelProperty(value = "循环付费结束时间")
    private LocalDate ending;

    @ApiModelProperty(value = "是否自动续费，数字含义由前端解析")
    private Integer renew;

    @ApiModelProperty(value = "周期计费的费用")
    private BigDecimal cost;

    @ApiModelProperty(value = "逻辑删除字段，标记是否被删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "记录创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "记录更新时间")
    private LocalDateTime updatedAt;


}
