package com.shiro.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.domain.vo.UsersDetailsVO;
import com.shiro.backend.domain.vo.UsersLoginVO;
import com.shiro.backend.utils.R;

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
