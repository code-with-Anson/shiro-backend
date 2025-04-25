package com.shiro.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.main.domain.dto.*;
import com.shiro.main.domain.po.Users;
import com.shiro.main.domain.vo.UsersDetailsVO;
import com.shiro.main.domain.vo.UsersLoginVO;
import com.shiro.main.utils.R;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
public interface IUsersService extends IService<Users> {
    public Users findUsersByEmail(String email);

    public void saveNewUser(AddUsersDTO addUsersDTO);

    UsersLoginVO login(LoginFormDTO loginFormDTO);

    R<String> forget(ForgetPasswordDTO forgetPasswordDTO);

    UsersLoginVO verifyAndLogin(CodeLoginDTO codeLoginDTO);

    UsersDetailsVO updateUser(UpdateUserDTO updateUserDTO);

    UsersDetailsVO getUserInfo();
}
