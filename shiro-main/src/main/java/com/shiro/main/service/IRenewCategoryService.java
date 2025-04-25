package com.shiro.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.main.domain.dto.*;
import com.shiro.main.domain.po.RenewCategory;
import com.shiro.main.domain.vo.QueryRenewBillVO;
import com.shiro.main.domain.vo.QueryRenewCategoryVO;
import com.shiro.main.utils.R;

/**
 * <p>
 * 循环付费账单分类表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
public interface IRenewCategoryService extends IService<RenewCategory> {
    /**
     * 添加新的循环账单分类
     *
     * @param addRenewCategoryDTO
     * @return
     */
    R<String> addNewRenewCategory(AddRenewCategoryDTO addRenewCategoryDTO);

    /**
     * 修改循环账单分类
     *
     * @param updateRenewCategoryDTO
     * @return
     */
    R<String> updateRenewCategory(UpdateRenewCategoryDTO updateRenewCategoryDTO);

    /**
     * 删除循环账单分类
     *
     * @param deleteRenewCategoryDTO
     * @return
     */
    R<String> deleteRenewCategory(DeleteRenewCategoryDTO deleteRenewCategoryDTO);

    /**
     * 查询用户循环账单分类
     *
     * @return
     */
    IPage<QueryRenewCategoryVO> getRenewCategory(PageDTO pageDTO);

    /**
     * 根据循环账单分类查找账单
     *
     * @param queryRenewCategoryBillDTO
     * @return
     */
    IPage<QueryRenewBillVO> queryRenewCategoryBill(QueryRenewCategoryBillDTO queryRenewCategoryBillDTO);
}
