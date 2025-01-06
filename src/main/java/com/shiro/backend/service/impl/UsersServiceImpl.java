package com.shiro.backend.service.impl;

import com.shiro.backend.domain.po.Users;
import com.shiro.backend.mapper.UsersMapper;
import com.shiro.backend.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
