package com.shiro.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.domain.vo.QueryBillsVO;
import com.shiro.backend.domain.vo.QueryCategoryVO;
import com.shiro.backend.utils.R;

/**
 * <p>
 * 账单分类表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
public interface ICategoryService extends IService<Category> {
    /**
     * 新增常规账单分类
     *
     * @param addCategoryDTO
     */
    R<String> addNewCategory(AddCategoryDTO addCategoryDTO);

    R<String> updateCategory(UpdateCategoryDTO updateCategoryDTO);

    /**
     * 查询用户常规账单分类
     *
     * @return
     */
    IPage<QueryCategoryVO> getCategory(PageDTO pageDTO);

    /**
     * 删除常规账单分类
     *
     * @param deleteCategoryDTO
     * @return
     */
    R<String> deleteCategory(DeleteCategoryDTO deleteCategoryDTO);

    /**
     * 根据常规账单分类查找账单
     *
     * @param queryCategoryBillsDTO
     * @return
     */
    IPage<QueryBillsVO> queryCategoryBills(QueryCategoryBillsDTO queryCategoryBillsDTO);
}
