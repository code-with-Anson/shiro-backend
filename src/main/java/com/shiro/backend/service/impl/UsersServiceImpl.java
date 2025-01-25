package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.config.DefaultCategories;
import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.domain.po.RenewCategory;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.domain.vo.UsersDetailsVO;
import com.shiro.backend.domain.vo.UsersLoginVO;
import com.shiro.backend.enums.CategoryType;
import com.shiro.backend.exception.CodeIsWrongException;
import com.shiro.backend.exception.EmailExistException;
import com.shiro.backend.exception.PasswordErrorException;
import com.shiro.backend.exception.UserNotFoundException;
import com.shiro.backend.mapper.UsersMapper;
import com.shiro.backend.service.ICategoryService;
import com.shiro.backend.service.IRenewCategoryService;
import com.shiro.backend.service.IUsersService;
import com.shiro.backend.utils.JwtUtil;
import com.shiro.backend.utils.R;
import com.shiro.backend.utils.UserContext;
import com.shiro.backend.utils.VerificationCode.VerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static com.shiro.backend.constant.MessageConstant.*;
import static com.shiro.backend.enums.Gender.OTHER;

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

    // 定义缓存的命名空间，方便管理
    private static final String CACHE_NAMESPACE = "userinfo";
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ICategoryService categoryService;
    private final IRenewCategoryService renewCategoryService;
    private final VerificationCode verificationCode;
    private final DefaultCategories defaultCategories;
    @Value("${shiro.jwt.secret-expire}")
    private Duration tokenTtl;
    @Value("${user.default-renew-category}")
    private String defaultRenewCategory;


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
        // 1. 将DTO转换为实体并设置密码加密
        Users user = addUsersDTO.toEntity();
        user.setPassword(passwordEncoder.encode(addUsersDTO.getPassword()));

        // 2. 检查邮箱是否已存在
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getEmail, addUsersDTO.getEmail());
        List<Users> usersList = usersMapper.selectList(queryWrapper);
        if (!usersList.isEmpty()) {
            throw new EmailExistException(MessageConstant.EMAIL_EXIST);
        }

        // 3. 设置默认性别并保存用户
        user.setSex(OTHER);
        save(user);

        // 4. 批量创建支出分类
        for (DefaultCategories.CategoryConfig config : defaultCategories.getExpense()) {
            Category category = new Category();
            category.setUserId(user.getId());
            category.setName(config.getName());
            category.setCategoryType(CategoryType.EXPENSE);
            // 暂时不做排序
            // category.setSort(config.getSort());
            categoryService.save(category);
        }

        // 5. 批量创建收入分类
        for (DefaultCategories.CategoryConfig config : defaultCategories.getIncome()) {
            Category category = new Category();
            category.setUserId(user.getId());
            category.setName(config.getName());
            category.setCategoryType(CategoryType.INCOME);
            // 暂时不做排序
            // category.setSort(config.getSort());
            categoryService.save(category);
        }

        // 6. 创建默认的循环账单分类
        RenewCategory renewCategory = new RenewCategory();
        renewCategory.setUserId(user.getId());
        renewCategory.setName(defaultRenewCategory);
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
        usersLoginVO.setEmail(user.getEmail());
        usersLoginVO.setName(user.getName());
        usersLoginVO.setSex(user.getSex());
        usersLoginVO.setAvatar(user.getAvatar());
        return usersLoginVO;
    }


    @Override
    public R<String> forget(ForgetPasswordDTO forgetPasswordDTO) {
        //1.数据校验
        String email = forgetPasswordDTO.getEmail();
        //2.确保存在账户
        Users user = lambdaQuery().eq(Users::getEmail, email).one();
        if (user == null) {
            throw new UserNotFoundException(ACCOUNT_NOT_FOUND);
        }
        //3.生成验证码
        verificationCode.generateAndSendCode(email, user.getId());
        return R.success("验证码已发送到您的邮箱，请查收");
    }

    @Override
    public UsersLoginVO verifyAndLogin(CodeLoginDTO codeLoginDTO) {
        //1.数据校验
        if (!verificationCode.verifyCode(codeLoginDTO.getEmail(), codeLoginDTO.getCode())) {
            throw new CodeIsWrongException(CODE_IS_WRONG);
        }
        //2.查找用户
        Users user = lambdaQuery().eq(Users::getEmail, codeLoginDTO.getEmail()).one();
        //3.封装成VO返回给前端
        UsersLoginVO usersLoginVO = new UsersLoginVO();
        usersLoginVO.setUserId(user.getId());
        usersLoginVO.setToken(jwtUtil.createToken(user.getId(), tokenTtl));
        usersLoginVO.setEmail(user.getEmail());
        usersLoginVO.setName(user.getName());
        usersLoginVO.setSex(user.getSex());
        usersLoginVO.setAvatar(user.getAvatar());
        return usersLoginVO;
    }

    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser()")
    @Override
    public UsersDetailsVO updateUser(UpdateUserDTO updateUserDTO) {
        Long userId = UserContext.getUser();

        Users findUser = lambdaQuery().eq(Users::getEmail, updateUserDTO.getEmail()).one();
        if (findUser != null) {
            throw new UserNotFoundException(EMAIL_EXIST);
        }
        Users users = new Users();
        users.setId(userId);
        if (updateUserDTO.getName() != null) {
            users.setName(updateUserDTO.getName());
        }

        if (updateUserDTO.getEmail() != null) {
            users.setEmail(updateUserDTO.getEmail());
        }

        if (updateUserDTO.getSex() != null) {
            users.setSex(updateUserDTO.getSex());
        }
        if (updateUserDTO.getPassword() != null) {
            users.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        if (updateUserDTO.getAvatar() != null) {
            users.setAvatar(updateUserDTO.getAvatar());
        }

        updateById(users);

        Users updatedUsers = getById(userId);
        UsersDetailsVO usersDetailsVO = new UsersDetailsVO();
        usersDetailsVO.setUserId(updatedUsers.getId());
        usersDetailsVO.setEmail(updatedUsers.getEmail());
        usersDetailsVO.setName(updatedUsers.getName());
        usersDetailsVO.setSex(updatedUsers.getSex());
        usersDetailsVO.setAvatar(updatedUsers.getAvatar());

        return usersDetailsVO;
    }

    @Cacheable(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser()",
            unless = "#result == null")
    @Override
    public UsersDetailsVO getUserInfo() {
        Long userId = UserContext.getUser();
        Users users = getById(userId);
        UsersDetailsVO usersDetailsVO = new UsersDetailsVO();
        usersDetailsVO.setUserId(users.getId());
        usersDetailsVO.setEmail(users.getEmail());
        usersDetailsVO.setName(users.getName());
        usersDetailsVO.setSex(users.getSex());
        usersDetailsVO.setAvatar(users.getAvatar());

        return usersDetailsVO;
    }
}
