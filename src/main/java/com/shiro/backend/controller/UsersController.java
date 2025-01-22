package com.shiro.backend.controller;


import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.vo.UsersDetailsVO;
import com.shiro.backend.domain.vo.UsersLoginVO;
import com.shiro.backend.service.IUsersService;
import com.shiro.backend.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * @param addUsersDTO
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody AddUsersDTO addUsersDTO) {
        //saveNewUser内部集成了重复检测，通过抛出异常来出发全局异常拦截器返回给前端错误
        usersService.saveNewUser(addUsersDTO);
        return R.success("成功注册新用户");
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public UsersLoginVO login(@RequestBody LoginFormDTO loginFormDTO) {
        return usersService.login(loginFormDTO);
    }

    @ApiOperation("用户忘记密码")
    @PostMapping("/forget")
    public R<String> forget(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        return usersService.forget(forgetPasswordDTO);
    }

    @ApiOperation("通过验证码登录")
    @PostMapping("/code-login")
    public UsersLoginVO shortTokenLogin(@RequestBody CodeLoginDTO codeLoginDTO) {
        return usersService.verifyAndLogin(codeLoginDTO);
    }

    @ApiOperation("更改用户信息")
    @PostMapping("/update-user")
    public UsersDetailsVO updateAvatar(@RequestBody UpdateUserDTO updateUserDTO) {
        return usersService.updateUser(updateUserDTO);
    }


    @ApiOperation("常规获取用户信息")
    @GetMapping("/user-info")
    public UsersDetailsVO userInfo() {
        return usersService.getUserInfo();
    }
}
