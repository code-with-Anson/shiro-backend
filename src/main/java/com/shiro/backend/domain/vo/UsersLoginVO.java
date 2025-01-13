package com.shiro.backend.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 返回给前端的 用户登录 视觉对象
 */
@Data
@ApiModel(description = "用户登录")
public class UsersLoginVO {
    private Long userId;
    private String token;
}
