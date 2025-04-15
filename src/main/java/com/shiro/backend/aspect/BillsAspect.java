package com.shiro.backend.aspect;

import com.shiro.backend.domain.dto.DeleteBillsDTO;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.mapper.BillsMapper;
import com.shiro.backend.service.IBillMonthStatisticsService;
import com.shiro.backend.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class BillsAspect {
    // 线程局部变量，用于存储年月信息
    private static final ThreadLocal<Set<YearMonthPair>> YEAR_MONTHS = new ThreadLocal<>();
    private final IBillMonthStatisticsService billMonthStatisticsService;
    private final BillsMapper billsMapper;

    // 清理ThreadLocal变量的静态方法
    public static void clear() {
        YEAR_MONTHS.remove();
    }

    // 定义一个切点，用于匹配BillsServiceImpl类中的所有方法
    @Pointcut("execution(* com.shiro.backend..service.impl.BillsServiceImpl.*(..))")
    public void billsServicePointcut() {
    }

    /**
     * 环绕通知，在同一事务中处理统计更新和业务操作
     * 这确保了原子性：要么统计和业务操作都成功，要么都失败
     */
    @Around("billsServicePointcut()")
    @Transactional(rollbackFor = Exception.class)
    public Object aroundBillsCrudOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("爱丽丝提示您：执行方法前 {}", methodName);

        // 初始化ThreadLocal变量
        YEAR_MONTHS.set(new HashSet<>());

        // 在执行业务操作前，处理需要提前更新统计的方法
        if (isStatisticsUpdateNeeded(methodName)) {
            updateStatisticsBeforeOperation(args, methodName);
        }

        // 执行原始方法
        Object result = joinPoint.proceed();

        // 对于非删除操作，在方法执行后更新统计
        if (!isDeleteOperation(methodName)) {
            updateStatisticsAfterOperation(args, methodName);
        }

        // 方法执行完毕，更新统计数据
        try {
            Long userId = UserContext.getUser();
            Set<YearMonthPair> yearMonths = YEAR_MONTHS.get();
            if (yearMonths != null && !yearMonths.isEmpty()) {
                updateYearMonthStatistics(userId, yearMonths);
            }
        } finally {
            // 清理ThreadLocal变量，防止内存泄漏
            clear();
        }

        log.info("爱丽丝提示您：执行方法后 {}", methodName);
        return result;
    }

    /**
     * 判断是否需要提前更新统计数据
     */
    private boolean isStatisticsUpdateNeeded(String methodName) {
        return "logicDeleteBills".equals(methodName) ||
                "realDeleteBills".equals(methodName);
    }

    /**
     * 判断是否为删除操作
     */
    private boolean isDeleteOperation(String methodName) {
        return "logicDeleteBills".equals(methodName) ||
                "realDeleteBills".equals(methodName);
    }

    /**
     * 在业务操作前更新统计数据
     * 主要处理删除操作
     */
    private void updateStatisticsBeforeOperation(Object[] args, String methodName) {
        try {
            log.info("爱丽丝提示您：在{}执行前收集统计信息", methodName);

            if (args.length > 0 && args[0] != null) {
                // 处理 DeleteBillsDTO
                if (args[0] instanceof DeleteBillsDTO) {
                    DeleteBillsDTO dto = (DeleteBillsDTO) args[0];
                    List<Long> billIds = dto.getBill_ids();

                    if (billIds != null && !billIds.isEmpty()) {
                        // 对于彻底删除操作，需要使用特殊方法获取账单信息
                        Set<YearMonthPair> yearMonths;
                        Long userId = UserContext.getUser();

                        if ("realDeleteBills".equals(methodName)) {
                            // 对于彻底删除，需要忽略逻辑删除标记获取账单
                            yearMonths = getYearMonthsForSpecialDeleteBills(billIds, userId);
                        } else {
                            // 对于普通逻辑删除，使用常规方法
                            yearMonths = getYearMonthsForBills(billIds);
                        }

                        log.info("爱丽丝提示您：{}操作涉及{}个账单，{}组年月统计",
                                methodName, billIds.size(), yearMonths.size());

                        // 将年月信息添加到ThreadLocal中，延迟执行统计更新
                        YEAR_MONTHS.get().addAll(yearMonths);
                    } else {
                        log.warn("爱丽丝提示您：操作包含空的账单ID列表");
                    }
                } else {
                    log.warn("爱丽丝提示您：参数类型不是预期的DeleteBillsDTO: {}", args[0].getClass().getName());
                }
            }
        } catch (Exception e) {
            log.error("爱丽丝提示您：在操作前收集统计信息出错", e);
        }
    }

    /**
     * 专门用于获取将要被彻底删除的账单信息（包括已逻辑删除的账单）
     */
    private Set<YearMonthPair> getYearMonthsForSpecialDeleteBills(List<Long> billIds, Long userId) {
        Set<YearMonthPair> yearMonths = new HashSet<>();
        try {
            if (billIds != null && !billIds.isEmpty()) {
                // 使用忽略逻辑删除标记的查询方法
                List<Bills> bills = billsMapper.selectBillsWithoutLogicDelete(billIds, userId);
                log.info("爱丽丝提示您：查询到{}个将被彻底删除的账单信息", bills.size());

                for (Bills bill : bills) {
                    if (bill != null && bill.getDate() != null) {
                        LocalDate date = bill.getDate();
                        // 添加调试日志
                        log.debug("爱丽丝提示您：账单 ID={}, 日期={}, 年={}, 月={}",
                                bill.getId(), date, date.getYear(), date.getMonthValue());

                        // 正确创建 YearMonthPair 实例
                        YearMonthPair pair = new YearMonthPair(date.getYear(), date.getMonthValue());
                        yearMonths.add(pair);
                    }
                }

                // 添加额外的调试日志
                log.debug("爱丽丝提示您：获取到 {} 个不同年月信息", yearMonths.size());
                for (YearMonthPair pair : yearMonths) {
                    log.debug("爱丽丝提示您：年月信息: {}-{}", pair.year, pair.month);
                }
            }
        } catch (Exception e) {
            log.error("爱丽丝提示您：获取将被彻底删除的账单年月信息失败", e);
        }
        return yearMonths;
    }

    /**
     * 在业务操作后更新统计数据
     * 主要处理新增和更新操作
     */
    private void updateStatisticsAfterOperation(Object[] args, String methodName) {
        try {
            log.info("爱丽丝提示您：在{}执行后收集统计信息", methodName);

            // 根据不同方法类型处理年月信息
            switch (methodName) {
                case "saveBills":
                    if (args[0] != null) {
                        // 处理保存账单后的统计更新
                        handleSaveBillsStatistics(args[0]);
                    }
                    break;
                case "updateBills":
                    if (args[0] != null) {
                        // 处理更新账单后的统计更新
                        handleUpdateBillsStatistics(args[0]);
                    }
                    break;
                case "recoverBills":
                    if (args[0] != null && args[0] instanceof DeleteBillsDTO) {
                        // 处理恢复账单的统计更新
                        DeleteBillsDTO dto = (DeleteBillsDTO) args[0];
                        List<Long> billIds = dto.getBill_ids();

                        if (billIds != null && !billIds.isEmpty()) {
                            // 由于是恢复操作，我们需要查询（包括已删除的账单）
                            Set<YearMonthPair> yearMonths = getYearMonthsForDeletedBills(billIds);
                            YEAR_MONTHS.get().addAll(yearMonths);
                        }
                    }
                    break;
                default:
                    log.info("爱丽丝提示您：方法{}不需要后续统计更新", methodName);
            }
        } catch (Exception e) {
            log.error("爱丽丝提示您：在操作后收集统计信息出错", e);
        }
    }

    /**
     * 处理保存账单后的统计更新
     */
    private void handleSaveBillsStatistics(Object arg) {
        try {
            // 从参数中提取日期信息
            LocalDate date = extractDateFromAddBillsDTO(arg);
            if (date != null) {
                YEAR_MONTHS.get().add(new YearMonthPair(date.getYear(), date.getMonthValue()));
            }
        } catch (Exception e) {
            log.error("爱丽丝提示您：处理保存账单统计时出错", e);
        }
    }

    /**
     * 处理更新账单后的统计更新
     */
    private void handleUpdateBillsStatistics(Object arg) {
        try {
            // 从参数中提取日期信息
            LocalDate date = extractDateFromUpdateBillsDTO(arg);
            if (date != null) {
                // 收集当前月的统计
                YEAR_MONTHS.get().add(new YearMonthPair(date.getYear(), date.getMonthValue()));

                // 如果可能，还需要更新原账单的月份统计（如果日期发生了变化）
                Long billId = extractIdFromUpdateBillsDTO(arg);
                if (billId != null) {
                    Bills bill = billsMapper.selectById(billId);
                    if (bill != null && bill.getDate() != null &&
                            !isSameMonth(bill.getDate(), date)) {
                        YEAR_MONTHS.get().add(new YearMonthPair(
                                bill.getDate().getYear(), bill.getDate().getMonthValue()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("爱丽丝提示您：处理更新账单统计时出错", e);
        }
    }

    /**
     * 更新年月对应的统计数据
     */
    private void updateYearMonthStatistics(Long userId, Set<YearMonthPair> yearMonths) {
        log.info("爱丽丝提示您：开始更新 {} 个年月的统计数据", yearMonths.size());

        // 添加安全检查
        if (yearMonths.isEmpty()) {
            log.warn("爱丽丝提示您：没有需要更新的年月统计数据");
            return;
        }

        for (Object obj : yearMonths) {
            try {
                if (!(obj instanceof YearMonthPair)) {
                    log.error("爱丽丝提示您：集合中存在非 YearMonthPair 类型对象: {}",
                            obj != null ? obj.getClass().getName() : "null");
                    continue;
                }

                YearMonthPair ym = (YearMonthPair) obj;
                // 检查年月值是否合理
                if (ym.year <= 0 || ym.month <= 0 || ym.month > 12) {
                    log.error("爱丽丝提示您：年月数据异常: 年={}, 月={}", ym.year, ym.month);
                    continue;
                }

                log.info("爱丽丝提示您：更新统计数据: {}-{}", ym.year, ym.month);
                billMonthStatisticsService.updateStatistics(userId, ym.year, ym.month);
            } catch (Exception e) {
                log.error("爱丽丝提示您：更新单个年月统计时出错", e);
            }
        }
    }

    /**
     * 获取账单列表涉及的所有年月（正常查询，遵循逻辑删除）
     */
    private Set<YearMonthPair> getYearMonthsForBills(List<Long> billIds) {
        Set<YearMonthPair> yearMonths = new HashSet<>();
        try {
            if (billIds != null && !billIds.isEmpty()) {
                // 使用mapper查询账单信息
                List<Bills> bills = billsMapper.selectBatchIds(billIds);
                log.info("爱丽丝提示您：查询到{}个账单信息", bills.size());

                for (Bills bill : bills) {
                    if (bill != null && bill.getDate() != null) {
                        yearMonths.add(new YearMonthPair(
                                bill.getDate().getYear(),
                                bill.getDate().getMonthValue()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("爱丽丝提示您：获取账单年月信息失败", e);
        }
        return yearMonths;
    }

    /**
     * 获取已逻辑删除的账单列表涉及的所有年月
     */
    private Set<YearMonthPair> getYearMonthsForDeletedBills(List<Long> billIds) {
        Set<YearMonthPair> yearMonths = new HashSet<>();
        try {
            if (billIds != null && !billIds.isEmpty()) {
                // 使用自定义方法查询包含已删除的账单
                Long userId = UserContext.getUser();
                List<Bills> bills = billsMapper.selectBillsWithoutLogicDelete(billIds, userId);
                log.info("爱丽丝提示您：查询到{}个被删除的账单信息", bills.size());

                for (Bills bill : bills) {
                    if (bill != null && bill.getDate() != null) {
                        yearMonths.add(new YearMonthPair(
                                bill.getDate().getYear(),
                                bill.getDate().getMonthValue()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("爱丽丝提示您：获取已删除账单年月信息失败", e);
        }
        return yearMonths;
    }

    /**
     * 从AddBillsDTO中提取日期
     */
    private LocalDate extractDateFromAddBillsDTO(Object dto) {
        try {
            // 使用反射安全地获取date属性
            java.lang.reflect.Method getDateMethod = dto.getClass().getMethod("getDate");
            return (LocalDate) getDateMethod.invoke(dto);
        } catch (Exception e) {
            log.error("爱丽丝提示您：从AddBillsDTO提取日期失败", e);
            return null;
        }
    }

    /**
     * 从UpdateBillsDTO中提取日期
     */
    private LocalDate extractDateFromUpdateBillsDTO(Object dto) {
        try {
            java.lang.reflect.Method getDateMethod = dto.getClass().getMethod("getDate");
            return (LocalDate) getDateMethod.invoke(dto);
        } catch (Exception e) {
            log.error("爱丽丝提示您：从UpdateBillsDTO提取日期失败", e);
            return null;
        }
    }

    /**
     * 从UpdateBillsDTO中提取ID
     */
    private Long extractIdFromUpdateBillsDTO(Object dto) {
        try {
            java.lang.reflect.Method getIdMethod = dto.getClass().getMethod("getId");
            return (Long) getIdMethod.invoke(dto);
        } catch (Exception e) {
            log.error("爱丽丝提示您：从UpdateBillsDTO提取ID失败", e);
            return null;
        }
    }

    /**
     * 判断两个日期是否在同一个月份
     */
    private boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() &&
                date1.getMonthValue() == date2.getMonthValue();
    }

    /**
     * 年月对，用于存储和比较年月组合
     */
    private static class YearMonthPair {
        final int year;
        final int month;

        YearMonthPair(int year, int month) {
            this.year = year;
            this.month = month;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            YearMonthPair that = (YearMonthPair) o;
            return year == that.year && month == that.month;
        }

        @Override
        public int hashCode() {
            return 31 * year + month;
        }
    }
}