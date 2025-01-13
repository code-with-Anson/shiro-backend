package com.shiro.backend.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户登录-数据传输对象")
public class LoginFormDTO {
    @ApiModelProperty(value = "邮箱", required = true, example = "alice@alice.com")
    @NotNull(message = "邮箱不能为空")
    private String email;
    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;
}
