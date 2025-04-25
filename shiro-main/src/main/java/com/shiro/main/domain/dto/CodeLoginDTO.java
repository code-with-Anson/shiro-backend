package com.shiro.main.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户验证码登录-数据传输对象")
public class CodeLoginDTO {
    @ApiModelProperty(value = "用户邮箱", required = true, example = "2795757650@qq.com")
    @NotNull(message = "邮箱不能为空")
    private String email;
    @ApiModelProperty(value = "用户验证码", required = true, example = "123456")
    @NotNull(message = "验证码不能为空")
    private String code;
}
