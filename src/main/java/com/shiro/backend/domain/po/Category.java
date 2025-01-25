package com.shiro.backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shiro.backend.enums.CategoryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 账单分类表
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category")
@ApiModel(value = "账单分类实体类")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分类的名称")
    private String name;

    @ApiModelProperty(value = "分类类型，属于支出还是收入")
    private CategoryType categoryType;

    @ApiModelProperty(value = "外键，用于关联users表单的id")
    private Long userId;

    @ApiModelProperty(value = "创建时间，默认当前时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "修改时间，自动更新为当前时间")
    private LocalDateTime updatedAt;
}
