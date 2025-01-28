package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.po.RenewBill;
import com.shiro.backend.domain.po.RenewCategory;
import com.shiro.backend.domain.vo.QueryRenewBillVO;
import com.shiro.backend.domain.vo.QueryRenewCategoryVO;
import com.shiro.backend.exception.CategoryWrongArgsException;
import com.shiro.backend.mapper.RenewBillMapper;
import com.shiro.backend.mapper.RenewCategoryMapper;
import com.shiro.backend.service.IRenewCategoryService;
import com.shiro.backend.utils.R;
import com.shiro.backend.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 循环付费账单分类表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RenewCategoryServiceImpl extends ServiceImpl<RenewCategoryMapper, RenewCategory> implements IRenewCategoryService {
    // 定义缓存的命名空间，方便管理
    private static final String CACHE_NAMESPACE = "renewcategory";
    private final RenewBillMapper renewBillMapper;
    private final RenewCategoryMapper renewCategoryMapper;

    /**
     * 增加新的循环账单分类
     *
     * @param addRenewCategoryDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewCategoryList'")
    public R<String> addNewRenewCategory(AddRenewCategoryDTO addRenewCategoryDTO) {
        //1.获取当前用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        RenewCategory renewCategory = addRenewCategoryDTO.toEntity();
        renewCategory.setUserId(userId);
        //3.保存当前新增分类
        save(renewCategory);
        return R.success("成功添加！");
    }

    /**
     * 更新循环账单分类
     *
     * @param updateRenewCategoryDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewCategoryList'")
    public R<String> updateRenewCategory(UpdateRenewCategoryDTO updateRenewCategoryDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.构建更新循环分类实体
        RenewCategory renewCategory = updateRenewCategoryDTO.toEntity();
        LambdaQueryWrapper<RenewCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RenewCategory::getUserId, userId)
                .eq(RenewCategory::getId, renewCategory.getId());
        //3.执行更新操作
        renewCategoryMapper.update(renewCategory, queryWrapper);
        return R.success("更新成功");
    }

    /**
     * 批量删除循环账单分类
     *
     * @param deleteRenewCategoryDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewCategoryList'")
    @Transactional
    public R<String> deleteRenewCategory(DeleteRenewCategoryDTO deleteRenewCategoryDTO) {
        //1.拿到需要删除的分类对应的id
        List<Long> deleteRenewCategoryList = deleteRenewCategoryDTO.getRenewCategoryIds();
        //2.通过自定义SQL，批量查出这些分类下对应的账单，包括被逻辑删除的账单
        if (deleteRenewCategoryList == null || deleteRenewCategoryList.isEmpty()) {
            throw new CategoryWrongArgsException(MessageConstant.ARGS_LOCK);
        }
        Long userId = UserContext.getUser();
        log.info("开始查询分类关联账单, userId={}, categoryIds={}", userId, deleteRenewCategoryList);
        List<RenewBill> thisRenewCategotyBills =
                renewBillMapper
                        .selectAllRenewBillByRenewCategory(deleteRenewCategoryList, userId);
        //3.使用stream流得到所有账单的id
        List<Long> toDeleteRenewBillIds =
                thisRenewCategotyBills
                        .stream()
                        .map(RenewBill::getId).collect(Collectors.toList());
        if (!toDeleteRenewBillIds.isEmpty()) {
            //4.批量删除这些账单
            log.info("开始删除关联账单, billIds={}", toDeleteRenewBillIds);
            renewBillMapper.realDeleteRenewBill(toDeleteRenewBillIds, UserContext.getUser());
        }
        //5.批量删除分类
        log.info("开始删除分类, categoryIds={}", deleteRenewCategoryList);
        int deletedCount = renewCategoryMapper.deleteBatchIds(deleteRenewCategoryList);
        log.info("删除分类完成, 成功删除{}个分类", deletedCount);
        return R.success("成功删除");
    }

    /**
     * 分页查询用户循环账单分类
     *
     * @param pageDTO
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewCategoryList'",
            unless = "#result == null || #result.getRecords().isEmpty()")
    public IPage<QueryRenewCategoryVO> getRenewCategory(PageDTO pageDTO) {
        //1.获取当前用户
        Long userId = UserContext.getUser();
        // 2. 创建分页对象
        Page<RenewCategory> page = new Page<>(
                pageDTO.getCurrentPage(),
                pageDTO.getPageSize()
        );
        //3.创建条件构造器
        LambdaQueryWrapper<RenewCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RenewCategory::getUserId, userId);
        //4.执行分页查询
        Page<RenewCategory> renewCategoryPage = renewCategoryMapper.selectPage(page, queryWrapper);
        // 5. 转换结果为 VO
        return renewCategoryPage.convert(QueryRenewCategoryVO::toVO);
    }

    /**
     * 分页查询用户循环账单分类下的循环账单
     *
     * @param queryRenewCategoryBillDTO
     * @return
     */
    @Override
    public IPage<QueryRenewBillVO> queryRenewCategoryBill(QueryRenewCategoryBillDTO queryRenewCategoryBillDTO) {
        //1.获取要查询的分类id，用户id
        Long userid = UserContext.getUser();
        Long renewCategoryId = queryRenewCategoryBillDTO.getRenewCategoryId();

        //2.创建分页对象
        Page<RenewBill> page = new Page<>(
                queryRenewCategoryBillDTO.getCurrentPage(),
                queryRenewCategoryBillDTO.getPageSize()
        );

        //3.创建条件构造器
        LambdaQueryWrapper<RenewBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RenewBill::getCategoryId, renewCategoryId);

        //4.执行分页查询
        Page<RenewBill> renewBillPage = renewBillMapper.selectPage(page, queryWrapper);

        // 5. 转换结果
        return renewBillPage.convert(QueryRenewBillVO::toVO);
    }
}
