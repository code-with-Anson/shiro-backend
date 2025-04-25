package com.shiro.main.service;

import org.springframework.scheduling.annotation.Async;

public interface IAsyncBillStatisticsService {
    @Async
        // 这个方法不再使用，保留接口实现
    void updateMonthlyStatisticsForBill(Long userId, Long billId, String methodName);

    @Async
        // 这个方法不再使用，保留接口实现
    void updateMonthlyStatisticsByDate(Long userId, Integer year, Integer month);


}
