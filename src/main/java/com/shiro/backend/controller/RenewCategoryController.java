package com.shiro.backend.controller;


import com.shiro.backend.domain.dto.AddRenewCategoryDTO;
import com.shiro.backend.service.IRenewCategoryService;
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
 * 循环付费账单分类表 前端控制器
 * </p>
 *
 * @author Anson
 * @since 2025-01-12
 */
@Api(tags = "循环账单分类接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/renew-category")
public class RenewCategoryController {
    private final IRenewCategoryService renewCategoryService;

    @ApiOperation("添加循环账单分类")
    @PostMapping("/add")
    public R<String> addNewRenewCategory(@RequestBody AddRenewCategoryDTO addRenewCategoryDTO) {
        renewCategoryService.addNewRenewCategory(addRenewCategoryDTO);
        return R.success("成功添加！");
    }
}
