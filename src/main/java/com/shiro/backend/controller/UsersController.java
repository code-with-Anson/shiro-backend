package com.shiro.backend.controller;


import com.shiro.backend.domain.dto.LoginFormDTO;
import com.shiro.backend.domain.dto.UsersDTO;
import com.shiro.backend.domain.vo.UsersLoginVO;
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
     * 用户注册
     *
     * @param usersDTO
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R<String> register(UsersDTO usersDTO) {
        //saveNewUser内部集成了重复检测，通过抛出异常来出发全局异常拦截器返回给前端错误
        usersService.saveNewUser(usersDTO);
        return R.success("成功注册新用户");
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public UsersLoginVO login(LoginFormDTO loginFormDTO) {
        return usersService.login(loginFormDTO);
    }


}
