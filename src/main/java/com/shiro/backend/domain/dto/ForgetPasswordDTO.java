package com.shiro.backend.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户忘记密码-数据传输对象")
public class ForgetPasswordDTO {
    @ApiModelProperty(value = "邮箱", required = true, example = "alice@alice.com")
    @NotNull(message = "邮箱不能为空")
    private String email;
}
