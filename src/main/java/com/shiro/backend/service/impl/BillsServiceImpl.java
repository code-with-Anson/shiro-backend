package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.domain.dto.AddBillsDTO;
import com.shiro.backend.domain.dto.DeleteBillsDTO;
import com.shiro.backend.domain.dto.QueryMonthBillsDTO;
import com.shiro.backend.domain.dto.UpdateBillsDTO;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.domain.po.Category;
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
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements IBillsService {
    private final CategoryMapper categoryMapper;
    private final BillsMapper billsMapper;

    /**
     * 保存账单
     *
     * @param addBillsDTO
     */
    @Override
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
    public List<QueryBillsVO> queryBills(QueryMonthBillsDTO queryMonthBillsDTO) {
        //1.获取要查询的年份，月份，用户id
        int year = queryMonthBillsDTO.getYear();
        int month = queryMonthBillsDTO.getMonth();
        Long userid = UserContext.getUser();
        //2.创建条件构造器
        QueryWrapper<Bills> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("YEAR(date)={0} AND MONTH(date)={1} AND userid={2}", year, month, userid);
        //3.查询数据
        List<Bills> bills = billsMapper.selectList(queryWrapper);
        //4.空数据处理，数据转换处理
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
    public List<IsDeletedBillsVO> queryIsDeletedBills() {
        Long userId = UserContext.getUser();
        List<Bills> isDeletedBills = billsMapper.queryBillsByDeletedStatus(isDeletedEnum.isDeleted, userId);
        //4.空数据处理，数据转换处理
        if (isDeletedBills == null || isDeletedBills.isEmpty()) {
            return Collections.emptyList();
        }
        return isDeletedBills.stream().map(IsDeletedBillsVO::toVO).toList();
    }

    /**
     * 彻底删除账单
     *
     * @param deleteBillsDTO
     * @return
     */
    @Override
    public R<String> realDeleteBills(DeleteBillsDTO deleteBillsDTO) {
        Long userId = UserContext.getUser();
        billsMapper.realDeleteBills(deleteBillsDTO.getBill_ids(), userId);
        return R.success("删除成功");
    }


}
