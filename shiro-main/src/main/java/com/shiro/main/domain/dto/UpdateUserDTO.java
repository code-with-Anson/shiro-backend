package com.shiro.main.domain.dto;

import com.shiro.main.enums.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "更新用户-数据传输对象", description = "用来传输更新用户的属性")
public class UpdateUserDTO {

    @ApiModelProperty(value = "昵称，1到10个字符", required = false, example = "Alice")
    private String name;

    @ApiModelProperty(value = "加密'前'的密码", required = false, example = "123456")
    private String password;

    @ApiModelProperty(value = "性别，为男或女", required = false, example = "女")
    private Gender sex;

    @ApiModelProperty(value = "用户邮箱，存在唯一约束", required = false, example = "123@123.com")
    private String email;

    @ApiModelProperty(value = "新的头像地址", required = false, example = "1")
    private String avatar;
}
