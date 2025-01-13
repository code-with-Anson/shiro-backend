package com.shiro.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.backend.domain.dto.AddCategoryDTO;
import com.shiro.backend.domain.dto.DeleteCategoryDTO;
import com.shiro.backend.domain.dto.PageDTO;
import com.shiro.backend.domain.dto.QueryCategoryBillsDTO;
import com.shiro.backend.domain.po.Bills;
import com.shiro.backend.domain.po.Category;
import com.shiro.backend.domain.vo.QueryCategoryVO;
import com.shiro.backend.domain.vo.QueryMonthBillsVO;
import com.shiro.backend.mapper.BillsMapper;
import com.shiro.backend.mapper.CategoryMapper;
import com.shiro.backend.mapper.UsersMapper;
import com.shiro.backend.service.ICategoryService;
import com.shiro.backend.utils.R;
import com.shiro.backend.utils.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 账单分类表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    private final CategoryMapper categoryMapper;
    private final BillsMapper billsMapper;
    private final UsersMapper usersMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper, BillsMapper billsMapper, UsersMapper usersMapper) {
        this.categoryMapper = categoryMapper;
        this.billsMapper = billsMapper;
        this.usersMapper = usersMapper;
    }

    @Override
    public R<String> addNewCategory(AddCategoryDTO addCategoryDTO) {
        //1.获取当前用户
        Long userId = UserContext.getUser();
        //2.创建实体类
        Category category = addCategoryDTO.toEntity();
        category.setUserId(userId);
        //3.保存当前新增分类
        save(category);
        return R.success("成功添加！");
    }

    @Override
    public IPage<QueryCategoryVO> getCategory(PageDTO pageDTO) {
        //1.获取当前用户
        Long userId = UserContext.getUser();

        // 2. 创建分页对象
        Page<Category> page = new Page<>(
                pageDTO.getCurrentPage(),
                pageDTO.getPageSize()
        );

        //3.创建条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getUserId, userId);

        //4.执行分页查询
        Page<Category> categoryPage = categoryMapper.selectPage(page, queryWrapper);

        // 5. 转换结果为 VO
        return categoryPage.convert(QueryCategoryVO::toVO);
    }

    @Override
    @Transactional
    public R<String> deleteCategory(DeleteCategoryDTO deleteCategoryDTO) {
        //1.拿到需要删除的分类对应的id
        List<Long> deleteCategoryList = deleteCategoryDTO.getCategoryIds();
        //2.通过自定义SQL，批量查出这些分类下对应的账单，包括被逻辑删除的账单
        Long userId = UserContext.getUser();
        List<Bills> thisCategotyBills = billsMapper.selectAllBillsByCategory(deleteCategoryList, userId);
        //3.使用stream流得到所有账单的id
        List<Long> toDeleteBillsIds =
                thisCategotyBills
                        .stream()
                        .map(Bills::getId).collect(Collectors.toList());
        //4.批量删除这些账单
        billsMapper.realDeleteBills(toDeleteBillsIds, UserContext.getUser());
        //5.批量删除分类
        categoryMapper.deleteBatchIds(deleteCategoryList);
        return R.success("成功删除");
    }

    @Override
    public IPage<QueryMonthBillsVO> queryCategoryBills(QueryCategoryBillsDTO queryCategoryBillsDTO) {
        //1.获取要查询的分类id，用户id
        Long userid = UserContext.getUser();
        Long categoryId = queryCategoryBillsDTO.getCategoryId();

        //2.创建分页对象
        Page<Bills> page = new Page<>(
                queryCategoryBillsDTO.getCurrentPage(),
                queryCategoryBillsDTO.getPageSize()
        );

        //3.创建条件构造器
        LambdaQueryWrapper<Bills> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bills::getCategoryId, categoryId);

        //4.执行分页查询
        Page<Bills> billsPage = billsMapper.selectPage(page, queryWrapper);

        // 5. 转换结果
        return billsPage.convert(QueryMonthBillsVO::toVO);
    }
}
