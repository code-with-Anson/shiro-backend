package com.shiro.main.domain.vo;

import com.shiro.main.enums.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回给前端的 常规获取用户信息 视觉对象
 */
@Data
@ApiModel(value = "常规获取用户信息-VO实体")
public class UsersDetailsVO implements Serializable {
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户的邮箱")
    private String email;
    @ApiModelProperty(value = "用户的昵称")
    private String name;
    @ApiModelProperty(value = "用户性别")
    private Gender sex;
    @ApiModelProperty(value = "用户头像地址")
    private String avatar;
}
