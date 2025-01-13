package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.domain.dto.AddCategoryDTO;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.mapper.CategoryMapper;
import com.shiro.backend.service.ICategoryService;
import com.shiro.backend.utils.UserContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账单分类表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public void addNewCategory(AddCategoryDTO addCategoryDTO) {
        //1.获取当前用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        Category category = addCategoryDTO.toEntity();
        category.setUserId(userId);
        //3.保存当前新增分类
        save(category);

    }
}
