package com.shiro.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiro.backend.domain.po.RenewBill;
import com.shiro.backend.enums.isDeletedEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 周期计费表，存储循环计费信息 Mapper 接口
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
public interface RenewBillMapper extends BaseMapper<RenewBill> {
    /**
     * 恢复被逻辑删除的循环账单
     *
     * @param isDeletedStatus
     * @param notDeletedStatus
     * @param ids
     */
    void recoverLogicDeletedRenewBill(@Param("isDeletedStatus") isDeletedEnum isDeletedStatus,
                                      @Param("notDeletedStatus") isDeletedEnum notDeletedStatus,
                                      @Param("ids") List<Long> ids);

    /**
     * 彻底删除循环账单
     *
     * @param renewBill_ids
     * @param userid
     */
    void realDeleteRenewBill(@Param("renewBill_ids") List<Long> renewBill_ids,
                             @Param("user_id") Long userid);

    /**
     * 按照循环分类查找所有的包含被逻辑删除的循环账单
     *
     * @param deleteRenewCategoryList
     * @param userId
     * @return
     */
    List<RenewBill> selectAllRenewBillByRenewCategory(@Param("deleteRenewCategoryList") List<Long> deleteRenewCategoryList,
                                                      @Param("user_id") Long userId);

    /**
     * 按照逻辑删除状态查找循环账单
     *
     * @param isDeletedStatus
     * @param userid
     * @return
     */
    Page<RenewBill> queryRenewBillByDeletedStatus(
            Page<RenewBill> page,
            @Param("isDeletedStatus") isDeletedEnum isDeletedStatus,
            @Param("user_id") Long userid
    );

}
