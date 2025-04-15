package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.domain.po.BillMonthStatistics;
import com.shiro.backend.mapper.BillMonthStatisticsMapper;
import com.shiro.backend.service.IBillMonthStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账单月度统计表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-04-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BillMonthStatisticsServiceImpl extends ServiceImpl<BillMonthStatisticsMapper, BillMonthStatistics> implements IBillMonthStatisticsService {
    private final BillMonthStatisticsMapper billMonthStatisticsMapper;

    @Override
    public void updateStatistics(Long userId, int year, int month) {
        try {
            billMonthStatisticsMapper.updateStatisticsForMonth(userId, year, month);
            log.info("已更新用户 {} 在 {}-{} 月的账单统计数据", userId, year, month);
        } catch (Exception e) {
            log.error("更新用户 {} 在 {}-{} 月的账单统计数据失败", userId, year, month, e);
        }
    }

}
