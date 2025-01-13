package com.shiro.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.AddCategoryDTO;
import com.shiro.backend.domain.dto.DeleteCategoryDTO;
import com.shiro.backend.domain.dto.PageDTO;
import com.shiro.backend.domain.dto.QueryCategoryBillsDTO;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.domain.vo.QueryCategoryVO;
import com.shiro.backend.domain.vo.QueryMonthBillsVO;
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
    IPage<QueryMonthBillsVO> queryCategoryBills(QueryCategoryBillsDTO queryCategoryBillsDTO);
}
