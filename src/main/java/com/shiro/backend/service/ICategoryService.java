package com.shiro.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.AddCategoryDTO;
import com.shiro.backend.domain.po.Category;

/**
 * <p>
 * 账单分类表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
public interface ICategoryService extends IService<Category> {
    void addNewCategory(AddCategoryDTO addCategoryDTO);
}
