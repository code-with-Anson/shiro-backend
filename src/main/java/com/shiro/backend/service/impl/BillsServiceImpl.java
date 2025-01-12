package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.domain.dto.BillsDTO;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.exception.CategoryDontExistException;
import com.shiro.backend.mapper.BillsMapper;
import com.shiro.backend.mapper.CategoryMapper;
import com.shiro.backend.service.IBillsService;
import com.shiro.backend.utils.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 账单记录表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements IBillsService {
    private final CategoryMapper categoryMapper;

    public BillsServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void saveBills(BillsDTO billsDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        Bills bills = billsDTO.toEntity();
        bills.setUserid(userId);
        //3.确保分类存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, billsDTO.getCategoryId());
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //4.存储账单
        if (categories == null || categories.isEmpty()) {
            throw new CategoryDontExistException("当前分类不存在");
        }
        save(bills);
    }
}
