package com.shiro.backend.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shiro.backend.enums.Gender;
import com.shiro.backend.enums.isDeletedEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel(value = "Users对象", description = "用户表")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID，唯一约束，由MybatisPlus使用雪花算法生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "昵称，1到10个字符")
    private String name;

    @ApiModelProperty(value = "加密后的密码")
    private String password;

    @ApiModelProperty(value = "性别，0是男性，1是女性，2是其他，存在Gender枚举类")
    private Gender sex;

    @ApiModelProperty(value = "用于存放用户的邮箱，唯一约束，邮箱是用户登录以及找回密码的重要凭据")
    private String email;

    @ApiModelProperty(value = "用户头像URL，默认URL由后端管理")
    private String avatar;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除字段，标记是否被删除")
    private isDeletedEnum isDeleted;

    @ApiModelProperty(value = "记录创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "记录更新时间")
    private LocalDateTime updatedAt;


}
