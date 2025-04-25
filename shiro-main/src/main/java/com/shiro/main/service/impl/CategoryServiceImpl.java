package com.shiro.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.main.domain.dto.*;
import com.shiro.main.domain.po.Bills;
import com.shiro.main.domain.po.Category;
import com.shiro.main.domain.vo.QueryBillsVO;
import com.shiro.main.domain.vo.QueryCategoryVO;
import com.shiro.main.mapper.BillsMapper;
import com.shiro.main.mapper.CategoryMapper;
import com.shiro.main.mapper.UsersMapper;
import com.shiro.main.service.ICategoryService;
import com.shiro.main.utils.R;
import com.shiro.main.utils.UserContext;
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
 * 账单分类表 服务实现类
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    // 定义缓存的命名空间，方便管理
    private static final String CACHE_NAMESPACE = "categories";
    private final CategoryMapper categoryMapper;
    private final BillsMapper billsMapper;
    private final UsersMapper usersMapper;

    /**
     * 增加新的常规账单分类
     *
     * @param addCategoryDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE, allEntries = true)
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

    /**
     * 更新常规账单分类
     *
     * @param updateCategoryDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE, allEntries = true)
    public R<String> updateCategory(UpdateCategoryDTO updateCategoryDTO) {
        //1.获取当前登录用户
        Long userId = UserContext.getUser();
        //2.构建更新循环分类实体
        Category category = updateCategoryDTO.toEntity();
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getUserId, userId)
                .eq(Category::getId, category.getId());
        //3.执行更新操作
        categoryMapper.update(category, queryWrapper);
        return R.success("更新成功");
    }

    /**
     * 分页查询常规账单分类
     *
     * @param pageDTO
     * @return
     */
    @Override
    @Cacheable(
            value = CACHE_NAMESPACE,
            key = "T(com.shiro.main.utils.UserContext).getUser()+':'+':page'+#pageDTO.currentPage+':'+#pageDTO.pageSize",
            unless = "#result == null || #result.getRecords().isEmpty()"
    )
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

    /**
     * 批量删除常规账单分类
     *
     * @param deleteCategoryDTO
     * @return
     */
    @Override
    @CacheEvict(value = CACHE_NAMESPACE, allEntries = true)
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
        if (!toDeleteBillsIds.isEmpty()) {
            //4.批量删除这些账单
            log.info("开始删除关联账单, billIds={}", toDeleteBillsIds);
            billsMapper.realDeleteBills(toDeleteBillsIds, UserContext.getUser());
        }
        //5.批量删除分类
        categoryMapper.deleteBatchIds(deleteCategoryList);
        return R.success("成功删除");
    }

    /**
     * 分页查询常规账单分类下的账单
     *
     * @param queryCategoryBillsDTO
     * @return
     */
    @Override
    public IPage<QueryBillsVO> queryCategoryBills(QueryCategoryBillsDTO queryCategoryBillsDTO) {
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
        return billsPage.convert(QueryBillsVO::toVO);
    }
}
