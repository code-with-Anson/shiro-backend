package com.shiro.backend.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.vo.QueryRenewBillVO;
import com.shiro.backend.domain.vo.QueryRenewCategoryVO;
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
        return renewCategoryService.addNewRenewCategory(addRenewCategoryDTO);
    }

    @ApiOperation("删除循环账单分类")
    @PostMapping("/delete")
    public R<String> deleteCategory(@RequestBody DeleteRenewCategoryDTO deleteRenewCategoryDTO) {
        return renewCategoryService.deleteRenewCategory(deleteRenewCategoryDTO);
    }

    @ApiOperation("修改循环账单分类")
    @PostMapping("/update")
    public R<String> updateCategory(@RequestBody UpdateRenewCategoryDTO UpdateRenewCategoryDTO) {
        renewCategoryService.updateRenewCategory(UpdateRenewCategoryDTO);
        return R.success("成功更新！");
    }

    @ApiOperation("分页获取循环账单分类")
    @PostMapping("/query")
    public IPage<QueryRenewCategoryVO> queryCategory(@RequestBody PageDTO pageDTO) {
        return renewCategoryService.getRenewCategory(pageDTO);
    }

    @ApiOperation("根据循环账单分类查找账单")
    @PostMapping("/query-bills")
    public IPage<QueryRenewBillVO> queryCategoryBills(@RequestBody QueryRenewCategoryBillDTO queryRenewCategoryBillDTO) {
        return renewCategoryService.queryRenewCategoryBill(queryRenewCategoryBillDTO);
    }
}
