package com.shiro.backend.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.vo.QueryCategoryVO;
import com.shiro.backend.domain.vo.QueryMonthBillsVO;
import com.shiro.backend.service.ICategoryService;
import com.shiro.backend.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 账单分类表 前端控制器
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Api(tags = "常规账单分类接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final ICategoryService categoryService;

    @ApiOperation("添加常规账单分类")
    @PostMapping("/add")
    public R<String> addNewCategory(@RequestBody AddCategoryDTO addCategoryDTO) {
        return categoryService.addNewCategory(addCategoryDTO);
    }

    @ApiOperation("删除常规账单分类")
    @PostMapping("/delete")
    public R<String> deleteCategory(@RequestBody DeleteCategoryDTO deleteCategoryDTO) {
        return categoryService.deleteCategory(deleteCategoryDTO);
    }

    @ApiOperation("修改常规账单分类")
    @PostMapping("/update")
    public R<String> updateCategory(@RequestBody UpdateCategoryDTO updateCategoryDTO) {
        categoryService.updateById(updateCategoryDTO.toEntity());
        return R.success("成功更新！");
    }

    @ApiOperation("获取常规账单分类")
    @PostMapping("/query")
    public IPage<QueryCategoryVO> queryCategory(@RequestBody PageDTO pageDTO) {
        return categoryService.getCategory(pageDTO);
    }

    @ApiOperation("根据常规账单分类查找账单")
    @PostMapping("/query-bills")
    public IPage<QueryMonthBillsVO> queryCategoryBills(@RequestBody QueryCategoryBillsDTO queryCategoryBillsDTO) {
        return categoryService.queryCategoryBills(queryCategoryBillsDTO);
    }
}
