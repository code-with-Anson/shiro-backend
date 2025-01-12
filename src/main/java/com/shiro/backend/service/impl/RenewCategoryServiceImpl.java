package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.domain.dto.AddRenewCategoryDTO;
import com.shiro.backend.domain.po.RenewCategory;
import com.shiro.backend.mapper.RenewCategoryMapper;
import com.shiro.backend.service.IRenewCategoryService;
import com.shiro.backend.utils.UserContext;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 循环付费账单分类表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Service
public class RenewCategoryServiceImpl extends ServiceImpl<RenewCategoryMapper, RenewCategory> implements IRenewCategoryService {
    @Override
    public void addNewRenewCategory(AddRenewCategoryDTO addRenewCategoryDTO) {
        //1.获取当前用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        RenewCategory renewCategory = addRenewCategoryDTO.toEntity();
        renewCategory.setUserId(userId);
        //3.保存当前新增分类
        save(renewCategory);
    }
}
