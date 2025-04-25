package com.shiro.main.service.impl;

import com.shiro.main.domain.po.Bills;
import com.shiro.main.mapper.BillsMapper;
import com.shiro.main.service.IAsyncBillStatisticsService;
import com.shiro.main.service.IBillMonthStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Slf4j
public class AsyncBillStatisticsServiceImpl implements IAsyncBillStatisticsService {

    private final BillsMapper billsMapper;
    private final IBillMonthStatisticsService billMonthStatisticsService;

    @Async
    @Override
    // 这个方法不再使用，保留接口实现
    public void updateMonthlyStatisticsForBill(Long userId, Long billId, String methodName) {
        try {
            log.info("异步处理账单统计更新，方法：{}，账单ID：{}", methodName, billId);

            // 根据ID查询账单，获取年月信息
            Bills bill = billsMapper.selectById(billId);
            if (bill == null) {
                log.warn("未找到ID为{}的账单，无法更新统计", billId);
                return;
            }

            LocalDate billDate = bill.getDate();
            int year = billDate.getYear();
            int month = billDate.getMonthValue();

            // 更新月度统计
            billMonthStatisticsService.updateStatistics(userId, year, month);
            log.info("已完成 {}-{} 月的账单统计数据异步更新", year, month);
        } catch (Exception e) {
            log.error("异步更新账单统计时发生错误", e);
        }
    }


    @Override
    // 这个方法不再使用，保留接口实现
    public void updateMonthlyStatisticsByDate(Long userId, Integer year, Integer month) {
        try {
            log.info("异步处理特定月份账单统计更新：{}-{}", year, month);
            billMonthStatisticsService.updateStatistics(userId, year, month);
            log.info("已完成 {}-{} 月的账单统计数据异步更新", year, month);
        } catch (Exception e) {
            log.error("异步更新特定月份账单统计时发生错误", e);
        }
    }
}
