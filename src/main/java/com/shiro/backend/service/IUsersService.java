package com.shiro.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.AddUsersDTO;
import com.shiro.backend.domain.dto.LoginFormDTO;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.domain.vo.UsersLoginVO;

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
}
