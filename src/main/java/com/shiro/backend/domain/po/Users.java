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
 * 用户表
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("users")
@ApiModel(value="Users对象", description="用户表")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID，唯一约束，由MybatisPlus使用雪花算法生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "昵称，1到10个字符")
    private String name;

    @ApiModelProperty(value = "加密后的密码")
    private String password;

    @ApiModelProperty(value = "性别，0是男性，1是女性，预留扩展数值支持其他选项")
    private Integer sex;

    @ApiModelProperty(value = "用户头像URL，默认URL由后端管理")
    private String avatar;

    @ApiModelProperty(value = "记录创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "记录更新时间")
    private LocalDateTime updatedAt;


}
