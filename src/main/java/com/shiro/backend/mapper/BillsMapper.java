package com.shiro.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.domain.po.RenewBill;
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
    //恢复被逻辑删除的账单
    void recoverLogicDeletedBills(@Param("isDeletedStatus") isDeletedEnum isDeletedStatus, @Param("notDeletedStatus") isDeletedEnum notDeletedStatus, @Param("ids") List<Long> ids);

    //批量根据逻辑删除状态查询账单
    Page<Bills> queryBillsByDeletedStatus(Page<RenewBill> page, @Param("isDeletedStatus") isDeletedEnum isDeletedStatus, @Param("userid") Long userid);

    //彻底删除账单
    void realDeleteBills(@Param("bill_ids") List<Long> bill_ids, @Param("userid") Long userid);

    //这个是用来根据分类查询账单的
    List<Bills> selectAllBillsByCategory(@Param("deleteCategoryList") List<Long> deleteCategoryList,
                                         @Param("userId") Long userId);

    //用来联表查询账单和分类
    List<Bills> selectBillsWithCategoryByMonth(@Param("year") int year,
                                               @Param("month") int month,
                                               @Param("userid") Long userid);
}
