package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.domain.po.RenewBill;
import com.shiro.backend.domain.vo.IsDeletedBillsVO;
import com.shiro.backend.domain.vo.QueryBillsVO;
import com.shiro.backend.enums.isDeletedEnum;
import com.shiro.backend.exception.CategoryDontExistException;
import com.shiro.backend.exception.DeleteBillsWrongArgsException;
import com.shiro.backend.mapper.BillsMapper;
import com.shiro.backend.mapper.CategoryMapper;
import com.shiro.backend.service.IBillsService;
import com.shiro.backend.utils.R;
import com.shiro.backend.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 账单记录表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@RequiredArgsConstructor
@Service
@Slf4j  // 添加日志支持，方便观察缓存行为
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements IBillsService {
    // 定义缓存的命名空间，方便管理
    private static final String CACHE_NAMESPACE = "bills";
    private final CategoryMapper categoryMapper;
    private final BillsMapper billsMapper;

    /**
     * 保存账单
     *
     * @param addBillsDTO
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE, key = "T(com.shiro.backend.utils.UserContext).getUser()  + ':' + #addBillsDTO.date.year + '-' + #addBillsDTO.date.monthValue")
    public R<String> saveBills(AddBillsDTO addBillsDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        Bills bills = addBillsDTO.toEntity();
        bills.setUserid(userId);
        //3.确保分类存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Category::getId, addBillsDTO.getCategoryId())
                .eq(Category::getUserId, bills.getUserid());
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //4.存储账单
        if (categories == null || categories.isEmpty()) {
            throw new CategoryDontExistException("当前分类不存在");
        }
        save(bills);
        return R.success("成功添加");
    }

    /**
     * 按照年月获取用户账单
     *
     * @param queryMonthBillsDTO
     * @return
     */
    @Override
    @Cacheable(
            value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':' + #queryMonthBillsDTO.year + '-' + #queryMonthBillsDTO.month",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<QueryBillsVO> queryBills(QueryMonthBillsDTO queryMonthBillsDTO) {
        //1.获取要查询的年份，月份，用户id
        int year = queryMonthBillsDTO.getYear();
        int month = queryMonthBillsDTO.getMonth();
        Long userid = UserContext.getUser();

        //2.使用联表查询方法获取账单及分类名称
        List<Bills> bills = billsMapper.selectBillsWithCategoryByMonth(year, month, userid);

        //3.空数据处理，数据转换处理
        if (bills == null || bills.isEmpty()) {
            return Collections.emptyList();
        }

        return bills.stream().map(QueryBillsVO::toVO).toList();
    }

    /**
     * 更新指定账单
     *
     * @param updateBillsDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE, key = "T(com.shiro.backend.utils.UserContext).getUser() + ':' + #updateBillsDTO.date.year + '-' + #updateBillsDTO.date.monthValue")
    public R<String> updateBills(UpdateBillsDTO updateBillsDTO) {
        Long userId = UserContext.getUser();
        Bills newBill = updateBillsDTO.toEntity();
        newBill.setUserid(userId);
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bills::getUserid, userId)
                .eq(Bills::getId, newBill.getId());
        billsMapper.update(newBill, queryWrapper);
        return R.failure("更新成功！");
    }

    /**
     * 逻辑删除用户账单
     *
     * @param deleteBillsDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE, key = "T(com.shiro.backend.utils.UserContext).getUser() + ':' + #updateBillsDTO.date.year + '-' + #updateBillsDTO.date.monthValue")
    public R<String> logicDeleteBills(DeleteBillsDTO deleteBillsDTO) {
        Long userId = UserContext.getUser();
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Bills::getUserid, userId)
                .in(Bills::getId, deleteBillsDTO.getBill_ids());
        if (deleteBillsDTO.getBill_ids().isEmpty()) {
            throw new DeleteBillsWrongArgsException(MessageConstant.ARGS_LOCK);
        }
        billsMapper.delete(queryWrapper);
        return R.success("删除成功");
    }

    /**
     * 恢复逻辑删除的账单
     *
     * @param deleteBillsDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE, key = "T(com.shiro.backend.utils.UserContext).getUser() + ':' + #updateBillsDTO.date.year + '-' + #updateBillsDTO.date.monthValue")
    public R<String> recoverBills(DeleteBillsDTO deleteBillsDTO) {
        Long userId = UserContext.getUser();
        billsMapper.recoverLogicDeletedBills(isDeletedEnum.isDeleted, isDeletedEnum.notDeleted, deleteBillsDTO.getBill_ids());
        return R.success("恢复成功");
    }

    /**
     * 获取被逻辑删除的账单
     *
     * @return
     */
    @Override
    public Page<IsDeletedBillsVO> queryIsDeletedBills(PageDTO pageDTO) {
        // 1.获取当前登录用户
        Long userId = UserContext.getUser();

        // 2.创建分页对象
        Page<RenewBill> page = new Page<>(pageDTO.getCurrentPage(), pageDTO.getPageSize());

        // 3.执行分页查询
        Page<Bills> billPage = billsMapper.queryBillsByDeletedStatus(
                page,
                isDeletedEnum.isDeleted,
                userId
        );

        // 4.转换结果
        List<IsDeletedBillsVO> voList = billPage.getRecords().stream()
                .map(IsDeletedBillsVO::toVO)
                .toList();

        // 5.封装返回结果
        Page<IsDeletedBillsVO> resultPage = new Page<>();
        resultPage.setRecords(voList);
        resultPage.setTotal(billPage.getTotal());
        resultPage.setCurrent(billPage.getCurrent());
        resultPage.setSize(billPage.getSize());

        return resultPage;
    }

    /**
     * 彻底删除账单
     *
     * @param deleteBillsDTO
     * @return
     */
    @Override
    @CacheEvict(value = {CACHE_NAMESPACE, CACHE_NAMESPACE + ":deleted"}, allEntries = true)
    public R<String> realDeleteBills(DeleteBillsDTO deleteBillsDTO) {
        Long userId = UserContext.getUser();
        log.info("物理删除账单: userId={}, billIds={}", UserContext.getUser(), deleteBillsDTO.getBill_ids());
        billsMapper.realDeleteBills(deleteBillsDTO.getBill_ids(), userId);
        return R.success("删除成功");
    }


}
