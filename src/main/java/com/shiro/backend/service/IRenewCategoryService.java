package com.shiro.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.AddRenewCategoryDTO;
import com.shiro.backend.domain.po.RenewCategory;

/**
 * <p>
 * 循环付费账单分类表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
public interface IRenewCategoryService extends IService<RenewCategory> {
    void addNewRenewCategory(AddRenewCategoryDTO addRenewCategoryDTO);
}
