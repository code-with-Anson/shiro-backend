package com.shiro.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.main.domain.po.BillMonthStatistics;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 账单月度统计表 Mapper 接口
 * </p>
 *
 * @author Anson
 * @since 2025-04-15
 */
public interface BillMonthStatisticsMapper extends BaseMapper<BillMonthStatistics> {

    /**
     * 根据年月和用户ID更新或插入月度统计数据
     *
     * @param userId 用户ID
     * @param year   年份
     * @param month  月份
     */
    void updateStatisticsForMonth(@Param("userId") Long userId,
                                  @Param("year") int year,
                                  @Param("month") int month);
}
