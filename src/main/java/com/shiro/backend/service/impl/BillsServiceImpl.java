package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.domain.dto.BillsDTO;
import com.shiro.backend.domain.dto.QueryMonthBillsDTO;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.domain.vo.QueryMonthBillsVO;
import com.shiro.backend.exception.CategoryDontExistException;
import com.shiro.backend.mapper.BillsMapper;
import com.shiro.backend.mapper.CategoryMapper;
import com.shiro.backend.service.IBillsService;
import com.shiro.backend.utils.UserContext;
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
@Service
public class BillsServiceImpl extends ServiceImpl<BillsMapper, Bills> implements IBillsService {
    private final CategoryMapper categoryMapper;
    private final BillsMapper billsMapper;

    public BillsServiceImpl(CategoryMapper categoryMapper, BillsMapper billsMapper) {
        this.categoryMapper = categoryMapper;
        this.billsMapper = billsMapper;
    }

    /**
     * 保存账单
     *
     * @param billsDTO
     */
    @Override
    public void saveBills(BillsDTO billsDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        Bills bills = billsDTO.toEntity();
        bills.setUserid(userId);
        //3.确保分类存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Category::getId, billsDTO.getCategoryId())
                .eq(Category::getUserId, bills.getUserid());
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //4.存储账单
        if (categories == null || categories.isEmpty()) {
            throw new CategoryDontExistException("当前分类不存在");
        }
        save(bills);
    }

    /**
     * 按照年月获取用户账单
     *
     * @param queryMonthBillsDTO
     * @return
     */
    @Override
    public List<QueryMonthBillsVO> queryBills(QueryMonthBillsDTO queryMonthBillsDTO) {
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
        return bills.stream().map(QueryMonthBillsVO::toVO).toList();
    }
}
