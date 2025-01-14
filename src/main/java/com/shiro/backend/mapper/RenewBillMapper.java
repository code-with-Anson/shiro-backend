package com.shiro.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    void recoverLogicDeletedRenewBill(@Param("isDeletedStatus") isDeletedEnum isDeletedStatus,
                                      @Param("notDeletedStatus") isDeletedEnum notDeletedStatus,
                                      @Param("ids") List<Long> ids);

    List<RenewBill> queryRenewBillByDeletedStatus(@Param("isDeletedStatus") isDeletedEnum isDeletedStatus,
                                                  @Param("user_id") Long userid);

    void realDeleteRenewBill(@Param("renewBill_ids") List<Long> renewBill_ids,
                             @Param("user_id") Long userid);

    List<RenewBill> selectAllRenewBillByRenewCategory(@Param("deleteRenewCategoryList") List<Long> deleteRenewCategoryList,
                                                      @Param("user_id") Long userId);
}
