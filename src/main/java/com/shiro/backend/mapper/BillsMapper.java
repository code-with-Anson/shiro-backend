package com.shiro.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.enums.isDeletedEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 账单记录表 Mapper 接口
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
public interface BillsMapper extends BaseMapper<Bills> {
    void recoverLogicDeletedBills(@Param("isDeletedStatus") isDeletedEnum isDeletedStatus, @Param("notDeletedStatus") isDeletedEnum notDeletedStatus, @Param("ids") List<Long> ids);

    List<Bills> queryBillsByDeletedStatus(@Param("isDeletedStatus") isDeletedEnum isDeletedStatus, @Param("userid") Long userid);

    void realDeleteBills(@Param("bill_ids") List<Long> bill_ids, @Param("userid") Long userid);

    List<Bills> selectAllBillsByCategory(@Param("deleteCategoryList") List<Long> deleteCategoryList,
                                         @Param("userId") Long userId);

}
