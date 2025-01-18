package com.shiro.backend.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回给前端的 用户登录 视觉对象
 */
@Data
@ApiModel(value = "用户登录-VO实体")
public class UsersLoginVO {
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "生成的token")
    private String token;
    @ApiModelProperty(value = "用户的邮箱")
    private String email;
    @ApiModelProperty(value = "用户的昵称")
    private String name;
}
