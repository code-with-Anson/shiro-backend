package com.shiro.main.controller;

import com.shiro.main.domain.dto.YearMonthDTO;
import com.shiro.main.domain.vo.BillStatisticsVO;
import com.shiro.main.service.IBillMonthStatisticsService;
import com.shiro.main.utils.R;
import com.shiro.main.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 账单月度统计表 前端控制器
 * </p>
 *
 * @author Anson
 * @since 2025-04-15
 */
@Api(tags = "账单统计相关接口")
@RestController
@RequestMapping("/bill-month-statistics")
@RequiredArgsConstructor
public class BillMonthStatisticsController {

    private final IBillMonthStatisticsService billMonthStatisticsService;

    @ApiOperation("获取指定年份或年月的账单统计数据")
    @PostMapping("/statistics")
    public R<BillStatisticsVO> getStatistics(@RequestBody YearMonthDTO yearMonthDTO) {
        // 获取当前用户ID
        Long userId = UserContext.getUser();

        // 调用服务获取统计数据
        BillStatisticsVO statistics = billMonthStatisticsService.getStatistics(
                userId,
                yearMonthDTO.getYear(),
                yearMonthDTO.getMonth()
        );

        return R.success(statistics);
    }
}