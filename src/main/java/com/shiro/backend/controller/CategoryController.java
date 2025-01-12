package com.shiro.backend.controller;


import com.shiro.backend.domain.dto.AddCategoryDTO;
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
        categoryService.addNewCategory(addCategoryDTO);
        return R.success("成功添加！");
    }
}
