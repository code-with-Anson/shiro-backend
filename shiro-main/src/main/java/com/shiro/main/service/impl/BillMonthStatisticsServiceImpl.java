package com.shiro.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiro.main.domain.po.BillMonthStatistics;
import com.shiro.main.domain.vo.BillStatisticsVO;
import com.shiro.main.mapper.BillMonthStatisticsMapper;
import com.shiro.main.service.IBillMonthStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void updateStatistics(Long userId, int year, int month) {
        try {
            billMonthStatisticsMapper.updateStatisticsForMonth(userId, year, month);
            log.info("已更新用户 {} 在 {}-{} 月的账单统计数据", userId, year, month);
        } catch (Exception e) {
            log.error("更新用户 {} 在 {}-{} 月的账单统计数据失败", userId, year, month, e);
        }
    }

    @Override
    public BillStatisticsVO getStatistics(Long userId, Integer year, Integer month) {
        try {
            if (month != null) {
                // 获取指定月份的统计数据
                return getMonthStatistics(userId, year, month);
            } else {
                // 获取全年统计数据
                return getYearStatistics(userId, year);
            }
        } catch (Exception e) {
            log.error("获取用户 {} 在 {} 年 {} 月的账单统计数据失败", userId, year, month, e);
            return createEmptyStatistics(year, month);
        }
    }

    /**
     * 获取指定月份的统计数据
     */
    private BillStatisticsVO getMonthStatistics(Long userId, Integer year, Integer month) {
        // 构建查询条件
        LambdaQueryWrapper<BillMonthStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillMonthStatistics::getUserId, userId)
                .eq(BillMonthStatistics::getYear, year)
                .eq(BillMonthStatistics::getMonth, month);

        // 查询数据
        BillMonthStatistics statistics = getOne(queryWrapper);

        // 如果没有找到数据，尝试生成一次
        if (statistics == null) {
            updateStatistics(userId, year, month);
            // 再次查询
            statistics = getOne(queryWrapper);
        }

        // 如果还是没有数据，返回空结果
        if (statistics == null) {
            return createEmptyStatistics(year, month);
        }

        // 转换为VO对象
        return convertToVO(statistics);
    }

    /**
     * 获取全年的统计数据
     */
    private BillStatisticsVO getYearStatistics(Long userId, Integer year) {
        // 查询该年所有月份的统计数据
        LambdaQueryWrapper<BillMonthStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillMonthStatistics::getUserId, userId)
                .eq(BillMonthStatistics::getYear, year)
                .orderByAsc(BillMonthStatistics::getMonth);

        List<BillMonthStatistics> monthStatsList = list(queryWrapper);

        // 如果没有数据，尝试生成1-12月的统计数据
        if (monthStatsList.isEmpty()) {
            for (int i = 1; i <= 12; i++) {
                updateStatistics(userId, year, i);
            }
            // 再次查询
            monthStatsList = list(queryWrapper);
        }

        // 创建全年统计结果
        BillStatisticsVO yearStats = new BillStatisticsVO();
        yearStats.setYear(year);
        yearStats.setMonth(null); // 全年统计没有具体月份
        yearStats.setTotalExpense(BigDecimal.ZERO);
        yearStats.setTotalIncome(BigDecimal.ZERO);
        yearStats.setBillCount(0);
        yearStats.setCategoryDetails(new HashMap<>());
        yearStats.setExpenseCategoryDetails(new HashMap<>());
        yearStats.setIncomeCategoryDetails(new HashMap<>());
        yearStats.setMonthDetails(new ArrayList<>());

        // 汇总所有月份数据
        for (BillMonthStatistics monthStat : monthStatsList) {
            // 累加基本数据
            yearStats.setTotalExpense(yearStats.getTotalExpense().add(monthStat.getTotalExpense()));
            yearStats.setTotalIncome(yearStats.getTotalIncome().add(monthStat.getTotalIncome()));
            yearStats.setBillCount(yearStats.getBillCount() + monthStat.getBillCount());

            // 合并分类明细
            mergeCategoryDetails(yearStats.getCategoryDetails(), parseJsonToMap(monthStat.getCategoryDetails()));
            mergeCategoryDetails(yearStats.getExpenseCategoryDetails(), parseJsonToMap(monthStat.getExpenseCategoryDetails()));
            mergeCategoryDetails(yearStats.getIncomeCategoryDetails(), parseJsonToMap(monthStat.getIncomeCategoryDetails()));

            // 添加月度汇总
            BillStatisticsVO.MonthSummary monthSummary = new BillStatisticsVO.MonthSummary();
            monthSummary.setMonth(monthStat.getMonth());
            monthSummary.setExpense(monthStat.getTotalExpense());
            monthSummary.setIncome(monthStat.getTotalIncome());
            monthSummary.setNetIncome(monthStat.getTotalIncome().subtract(monthStat.getTotalExpense()));
            monthSummary.setBillCount(monthStat.getBillCount());
            yearStats.getMonthDetails().add(monthSummary);
        }

        // 计算全年净收入
        yearStats.setNetIncome(yearStats.getTotalIncome().subtract(yearStats.getTotalExpense()));

        return yearStats;
    }

    /**
     * 将月度统计数据转换为VO对象
     */
    private BillStatisticsVO convertToVO(BillMonthStatistics statistics) {
        BillStatisticsVO vo = new BillStatisticsVO();
        vo.setYear(statistics.getYear());
        vo.setMonth(statistics.getMonth());
        vo.setTotalExpense(statistics.getTotalExpense());
        vo.setTotalIncome(statistics.getTotalIncome());
        vo.setNetIncome(statistics.getTotalIncome().subtract(statistics.getTotalExpense()));
        vo.setBillCount(statistics.getBillCount());

        // 解析JSON字符串为Map
        vo.setCategoryDetails(parseJsonToMap(statistics.getCategoryDetails()));
        vo.setExpenseCategoryDetails(parseJsonToMap(statistics.getExpenseCategoryDetails()));
        vo.setIncomeCategoryDetails(parseJsonToMap(statistics.getIncomeCategoryDetails()));

        return vo;
    }

    /**
     * 将JSON字符串解析为Map
     */
    private Map<String, BigDecimal> parseJsonToMap(String json) {
        try {
            if (json == null || json.isEmpty() || "{}".equals(json)) {
                return new HashMap<>();
            }
            return objectMapper.readValue(json, new TypeReference<Map<String, BigDecimal>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("解析JSON字符串失败: {}", json, e);
            return new HashMap<>();
        }
    }

    /**
     * 合并两个分类明细Map
     */
    private void mergeCategoryDetails(Map<String, BigDecimal> target, Map<String, BigDecimal> source) {
        if (source == null) return;

        source.forEach((category, amount) -> {
            target.merge(category, amount, BigDecimal::add);
        });
    }

    /**
     * 创建空的统计结果
     */
    private BillStatisticsVO createEmptyStatistics(Integer year, Integer month) {
        BillStatisticsVO vo = new BillStatisticsVO();
        vo.setYear(year);
        vo.setMonth(month);
        vo.setTotalExpense(BigDecimal.ZERO);
        vo.setTotalIncome(BigDecimal.ZERO);
        vo.setNetIncome(BigDecimal.ZERO);
        vo.setBillCount(0);
        vo.setCategoryDetails(new HashMap<>());
        vo.setExpenseCategoryDetails(new HashMap<>());
        vo.setIncomeCategoryDetails(new HashMap<>());

        if (month == null) {
            // 全年统计，提供空的月度明细
            vo.setMonthDetails(new ArrayList<>());
        }

        return vo;
    }
}