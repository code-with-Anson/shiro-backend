package com.shiro.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.dto.AddRenewBillDTO;
import com.shiro.backend.domain.dto.DeleteRenewBillDTO;
import com.shiro.backend.domain.dto.PageDTO;
import com.shiro.backend.domain.dto.UpdateRenewBillDTO;
import com.shiro.backend.domain.po.RenewBill;
import com.shiro.backend.domain.vo.IsDeletedRenewBillVO;
import com.shiro.backend.domain.vo.QueryRenewBillVO;
import com.shiro.backend.utils.R;

import java.util.List;

/**
 * <p>
 * 周期计费表，存储循环计费信息 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
public interface IRenewBillService extends IService<RenewBill> {
    /**
     * 保存循环账单
     *
     * @param addRenewBillDTO
     */
    R<String> saveRenewBill(AddRenewBillDTO addRenewBillDTO);

    /**
     * 分页查询循环账单
     *
     * @param pageDTO
     * @return
     */
    IPage<QueryRenewBillVO> queryRenewBill(PageDTO pageDTO);


    /**
     * 更新循环账单
     *
     * @param updateRenewBillDTO
     * @return
     */
    R<String> updateRenewBill(UpdateRenewBillDTO updateRenewBillDTO);


    /**
     * 批量逻辑删除循环账单
     *
     * @param deleteRenewBillDTO
     * @return
     */
    R<String> logicDeleteRenewBill(DeleteRenewBillDTO deleteRenewBillDTO);

    /**
     * 批量恢复逻辑删除的循环账单
     *
     * @param deleteRenewBillDTO
     * @return
     */
    R<String> recoverRenewBill(DeleteRenewBillDTO deleteRenewBillDTO);


    /**
     * 查询用户被逻辑删除的循环账单
     *
     * @return
     */
    List<IsDeletedRenewBillVO> queryIsDeletedRenewBill();

    /**
     * 彻底删除循环账单
     *
     * @param deleteRenewBillDTO
     * @return
     */
    R<String> realDeleteRenewBill(DeleteRenewBillDTO deleteRenewBillDTO);
}
