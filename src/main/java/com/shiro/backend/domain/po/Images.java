package com.shiro.backend.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 图片表，存储账单或周期计费相关的图片附件
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("images")
@ApiModel(value="Images对象", description="图片表，存储账单或周期计费相关的图片附件")
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片ID，主键，由后端生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "关联账单表的ID，外键，可以为空")
    private Long billId;

    @ApiModelProperty(value = "关联周期计费表的ID，外键，可以为空")
    private Long renewId;

    @ApiModelProperty(value = "图片地址，存储后端返回的URL")
    private String url;

    @ApiModelProperty(value = "逻辑删除字段，标记是否被删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "记录创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "记录更新时间")
    private LocalDateTime updatedAt;


}
