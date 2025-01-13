package com.shiro.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.AddBillsDTO;
import com.shiro.backend.domain.dto.DeleteBillsDTO;
import com.shiro.backend.domain.dto.QueryMonthBillsDTO;
import com.shiro.backend.domain.dto.UpdateBillsDTO;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.domain.vo.IsDeletedBillsVO;
import com.shiro.backend.domain.vo.QueryMonthBillsVO;
import com.shiro.backend.utils.R;

import java.util.List;

/**
 * <p>
 * 账单记录表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
public interface IBillsService extends IService<Bills> {
    /**
     * 保存账单
     *
     * @param addBillsDTO
     */
    R<String> saveBills(AddBillsDTO addBillsDTO);

    /**
     * 按照年月获取用户账单
     *
     * @param queryMonthBillsDTO
     * @return
     */
    List<QueryMonthBillsVO> queryBills(QueryMonthBillsDTO queryMonthBillsDTO);


    /**
     * 更新指定账单
     *
     * @param updateBillsDTO
     * @return
     */
    R<String> updateBills(UpdateBillsDTO updateBillsDTO);

    /**
     * 批量逻辑删除账单
     *
     * @param deleteBillsDTO
     * @return
     */
    R<String> logicDeleteBills(DeleteBillsDTO deleteBillsDTO);

    /**
     * 批量恢复逻辑删除的账单
     *
     * @param deleteBillsDTO
     * @return
     */
    R<String> recoverBills(DeleteBillsDTO deleteBillsDTO);


    /**
     * 查询用户被逻辑删除的账单
     *
     * @return
     */
    List<IsDeletedBillsVO> queryIsDeletedBills();

    /**
     * 彻底删除账单
     *
     * @param deleteBillsDTO
     * @return
     */
    R<String> realDeleteBills(DeleteBillsDTO deleteBillsDTO);
}
