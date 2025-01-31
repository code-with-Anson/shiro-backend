package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.domain.dto.AddRenewBillDTO;
import com.shiro.backend.domain.dto.DeleteRenewBillDTO;
import com.shiro.backend.domain.dto.PageDTO;
import com.shiro.backend.domain.dto.UpdateRenewBillDTO;
import com.shiro.backend.domain.po.RenewBill;
import com.shiro.backend.domain.po.RenewCategory;
import com.shiro.backend.domain.vo.IsDeletedRenewBillVO;
import com.shiro.backend.domain.vo.QueryRenewBillVO;
import com.shiro.backend.enums.isDeletedEnum;
import com.shiro.backend.exception.CategoryDontExistException;
import com.shiro.backend.exception.DeleteBillsWrongArgsException;
import com.shiro.backend.mapper.RenewBillMapper;
import com.shiro.backend.mapper.RenewCategoryMapper;
import com.shiro.backend.service.IRenewBillService;
import com.shiro.backend.utils.R;
import com.shiro.backend.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 周期计费表，存储循环计费信息 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Service
@RequiredArgsConstructor
public class RenewBillServiceImpl extends ServiceImpl<RenewBillMapper, RenewBill> implements IRenewBillService {
    // 定义缓存的命名空间，方便管理
    private static final String CACHE_NAMESPACE = "renewbill";
    private final RenewCategoryMapper renewCategoryMapper;
    private final RenewBillMapper renewBillMapper;

    /**
     * 添加循环账单
     *
     * @param addRenewBillDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewBillList'")
    public R<String> saveRenewBill(AddRenewBillDTO addRenewBillDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        RenewBill renewBill = addRenewBillDTO.toEntity();
        renewBill.setUserId(userId);
        //3.确保分类存在
        LambdaQueryWrapper<RenewCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(RenewCategory::getId, addRenewBillDTO.getCategoryId())
                .eq(RenewCategory::getUserId, renewBill.getUserId());
        List<RenewCategory> renewCategories = renewCategoryMapper.selectList(queryWrapper);
        //4.存储账单
        if (renewCategories == null || renewCategories.isEmpty()) {
            throw new CategoryDontExistException("当前分类不存在");
        }
        save(renewBill);
        return R.success("成功添加");
    }

    /**
     * 分页查询循环账单
     *
     * @param pageDTO
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewBillList'",
            unless = "#result == null || #result.getRecords().isEmpty()"
    )
    public IPage<QueryRenewBillVO> queryRenewBill(PageDTO pageDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        // 2. 创建分页对象
        Page<RenewBill> page = new Page<>(
                pageDTO.getCurrentPage(),
                pageDTO.getPageSize()
        );
        //3.创建条件构造器
        LambdaQueryWrapper<RenewBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RenewBill::getUserId, userId);
        //4.执行分页查询
        Page<RenewBill> renewBillPage = renewBillMapper.selectPage(page, queryWrapper);
        // 5. 转换结果为 VO
        return renewBillPage.convert(QueryRenewBillVO::toVO);
    }

    /**
     * 更新循环账单
     *
     * @param updateRenewBillDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewBillList'")
    public R<String> updateRenewBill(UpdateRenewBillDTO updateRenewBillDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.构造新的循环账单实体
        RenewBill renewBill = updateRenewBillDTO.toEntity();
        renewBill.setUserId(userId);
        //3.创建条件构造器
        LambdaQueryWrapper<RenewBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RenewBill::getUserId, userId)
                .eq(RenewBill::getId, renewBill.getId());
        //4.更新循环账单
        renewBillMapper.update(renewBill, queryWrapper);
        return R.success("更新成功！");
    }

    /**
     * 逻辑删除用户循环账单
     *
     * @param deleteRenewBillDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewBillList'")
    public R<String> logicDeleteRenewBill(DeleteRenewBillDTO deleteRenewBillDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.创建条件构造器
        LambdaQueryWrapper<RenewBill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RenewBill::getUserId, userId)
                .in(RenewBill::getId, deleteRenewBillDTO.getRenewBill_ids());
        if (deleteRenewBillDTO.getRenewBill_ids().isEmpty()) {
            throw new DeleteBillsWrongArgsException(MessageConstant.ARGS_LOCK);
        }
        //3.删除循环账单
        renewBillMapper.delete(queryWrapper);
        return R.success("删除成功");
    }

    /**
     * 恢复被逻辑删除的循环账单
     *
     * @param deleteRenewBillDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewBillList'")
    public R<String> recoverRenewBill(DeleteRenewBillDTO deleteRenewBillDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.恢复被逻辑删除的循环账单
        renewBillMapper.recoverLogicDeletedRenewBill(isDeletedEnum.isDeleted, isDeletedEnum.notDeleted, deleteRenewBillDTO.getRenewBill_ids());
        return R.success("恢复成功");
    }

    /**
     * 查询被逻辑删除的循环账单
     *
     * @return
     */
    @Override
    public Page<IsDeletedRenewBillVO> queryIsDeletedRenewBill(PageDTO pageDTO) {
        // 1.获取当前登录用户
        Long userId = UserContext.getUser();

        // 2.创建分页对象
        Page<RenewBill> page = new Page<>(pageDTO.getCurrentPage(), pageDTO.getPageSize());

        // 3.执行分页查询
        Page<RenewBill> billPage = renewBillMapper.queryRenewBillByDeletedStatus(
                page,
                isDeletedEnum.isDeleted,
                userId
        );

        // 4.转换结果
        List<IsDeletedRenewBillVO> voList = billPage.getRecords().stream()
                .map(IsDeletedRenewBillVO::toVO)
                .toList();

        // 5.封装返回结果
        Page<IsDeletedRenewBillVO> resultPage = new Page<>();
        resultPage.setRecords(voList);
        resultPage.setTotal(billPage.getTotal());
        resultPage.setCurrent(billPage.getCurrent());
        resultPage.setSize(billPage.getSize());

        return resultPage;
    }

    /**
     * 彻底删除循环账单
     *
     * @param deleteRenewBillDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE,
            key = "T(com.shiro.backend.utils.UserContext).getUser() + ':renewBillList'")
    public R<String> realDeleteRenewBill(DeleteRenewBillDTO deleteRenewBillDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.调用自定义SQL彻底删除循环账单
        renewBillMapper.realDeleteRenewBill(deleteRenewBillDTO.getRenewBill_ids(), userId);
        return R.success("删除成功");
    }
}
