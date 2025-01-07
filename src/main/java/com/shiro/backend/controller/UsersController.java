package com.shiro.backend.controller;


import com.shiro.backend.domain.dto.UsersDTO;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.service.IUsersService;
import com.shiro.backend.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final IUsersService usersService;

    /**
     * 新增用户
     *
     * @param usersDTO
     * @return
     */
    @ApiOperation("新增用户")
    @PostMapping("/register")
    public R<String> register(UsersDTO usersDTO) {
        // 转换 DTO 到实体
        Users newUser = usersDTO.toEntity();
        usersService.save(newUser);
        return R.success("成功注册新用户");
    }


}
