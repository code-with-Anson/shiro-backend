package com.shiro.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.backend.domain.po.BillMonthStatistics;

/**
 * <p>
 * 账单月度统计表 服务类
 * </p>
 *
 * @author Anson
 * @since 2025-04-15
 */
public interface IBillMonthStatisticsService extends IService<BillMonthStatistics> {
    /**
     * 更新指定年月的统计数据
     *
     * @param userId 用户ID
     * @param year   年份
     * @param month  月份
     */
    void updateStatistics(Long userId, int year, int month);
}
