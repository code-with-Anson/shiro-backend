package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.domain.dto.UsersDTO;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.exception.EmailExistException;
import com.shiro.backend.mapper.UsersMapper;
import com.shiro.backend.service.IUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    private final UsersMapper usersMapper;

    @Override
    public Users findUsersByEmail(String email) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getEmail, email);
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        return usersList.isEmpty() ? null : usersList.get(0);
    }

    @Override
    public void saveNewUser(UsersDTO usersDTO) {
        //将得到的DTO数据传输对象转换为实体
        Users newUser = usersDTO.toEntity();
        //构造自定义查询wrapper
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getEmail, usersDTO.getEmail());
        //收集查询到的数据
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        //如果已经存在当前邮箱则抛出异常
        if (!usersList.isEmpty()) {
            throw new EmailExistException(MessageConstant.EMAIL_EXIST);
        }
        //插入新用户并返回用户
        usersMapper.insert(newUser);
    }
}
