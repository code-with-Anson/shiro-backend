package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.domain.dto.AddUsersDTO;
import com.shiro.backend.domain.dto.LoginFormDTO;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.domain.po.RenewCategory;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.domain.vo.UsersLoginVO;
import com.shiro.backend.exception.EmailExistException;
import com.shiro.backend.exception.PasswordErrorException;
import com.shiro.backend.exception.UserNotFoundException;
import com.shiro.backend.mapper.UsersMapper;
import com.shiro.backend.service.ICategoryService;
import com.shiro.backend.service.IRenewCategoryService;
import com.shiro.backend.service.IUsersService;
import com.shiro.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static com.shiro.backend.constant.MessageConstant.ACCOUNT_NOT_FOUND;

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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ICategoryService categoryService;
    private final IRenewCategoryService renewCategoryService;
    @Value("${shiro.jwt.secret-expire}")
    private Duration tokenTtl;

    @Override
    public Users findUsersByEmail(String email) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getEmail, email);
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        return usersList.isEmpty() ? null : usersList.get(0);
    }

    @Override
    @Transactional
    public void saveNewUser(AddUsersDTO addUsersDTO) {
        //1.将得到的DTO数据传输对象转换为实体
        Users user = addUsersDTO.toEntity();
        user.setPassword(passwordEncoder.encode(addUsersDTO.getPassword()));
        //2.构造自定义查询wrapper
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getEmail, addUsersDTO.getEmail());
        //3.收集查询到的数据
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        //4.如果已经存在当前邮箱则抛出异常
        if (!usersList.isEmpty()) {
            throw new EmailExistException(MessageConstant.EMAIL_EXIST);
        }
        //5.插入新用户
        save(user);

        //6.给新用户创建默认的常规账单分类和循环账单分类
        Category category = new Category();
        RenewCategory renewCategory = new RenewCategory();

        category.setUserId(user.getId());
        category.setName("汐落");
        renewCategory.setUserId(user.getId());
        renewCategory.setName("汐落");

        categoryService.save(category);
        renewCategoryService.save(renewCategory);
    }

    @Override
    public UsersLoginVO login(LoginFormDTO loginFormDTO) {
        //1.数据校验
        String email = loginFormDTO.getEmail();
        String password = loginFormDTO.getPassword();
        //2.确保存在账户
        Users user = lambdaQuery().eq(Users::getEmail, email).one();
        if (user == null) {
            throw new UserNotFoundException(ACCOUNT_NOT_FOUND);
        }
        //3.校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //4.生成TOKEN
        String token = jwtUtil.createToken(user.getId(), tokenTtl);
        //5.封装成VO返回给前端
        UsersLoginVO usersLoginVO = new UsersLoginVO();
        usersLoginVO.setUserId(user.getId());
        usersLoginVO.setToken(token);
        return usersLoginVO;
    }
}
