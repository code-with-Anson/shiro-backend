package com.shiro.backend.domain.dto;

import com.shiro.backend.domain.po.Users;
import com.shiro.backend.enums.Gender;
import com.shiro.backend.enums.isDeletedEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@ApiModel(description = "用户注册-数据传输对象")
public class UsersDTO {

    @ApiModelProperty(value = "昵称，1到10个字符", required = true, example = "Alice")
    private String name;

    @ApiModelProperty(value = "加密后的密码", required = true, example = "123456")
    private String password;

    @ApiModelProperty(value = "性别，为男或女", required = true, example = "女")
    private Gender sex;

    @ApiModelProperty(value = "用户邮箱，存在唯一约束", required = true, example = "123@123.com")
    private String email;

    @ApiModelProperty(value = "用户头像URL，这里在application有定义默认地址，并在DTO转换成Users中使用了默认地址")
    private String avatar;

    @ApiModelProperty(hidden = true)
    @Value("${user.default-avatar}")
    private String defaultAvatar;

    public Users toEntity() {
        Users user = new Users();
        user.setName(this.name);
        user.setPassword(this.password);
        user.setSex(this.sex);
        user.setEmail(this.email);
        user.setAvatar(defaultAvatar);
        user.setIsDeleted(isDeletedEnum.notDeleted);
        return user;
    }
}
