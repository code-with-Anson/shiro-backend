package com.shiro.backend.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiro.backend.domain.dto.*;
import com.shiro.backend.domain.vo.IsDeletedBillsVO;
import com.shiro.backend.domain.vo.QueryBillsVO;
import com.shiro.backend.service.IBillsService;
import com.shiro.backend.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 账单记录表 前端控制器
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Api(tags = "常规账单相关接口")
@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillsController {
    private final IBillsService billsService;

    @ApiOperation("记录账单")
    @PostMapping("/add")
    public R<String> addNewBill(@RequestBody AddBillsDTO addBillsDTO) {
        return billsService.saveBills(addBillsDTO);
    }

    @ApiOperation("按照年月获取用户账单")
    @PostMapping("/month")
    public List<QueryBillsVO> queryBills(@RequestBody QueryMonthBillsDTO queryMonthBillsDTO) {
        return billsService.queryBills(queryMonthBillsDTO);
    }

    @ApiOperation("更新账单")
    @PostMapping("/update")
    public R<String> updateBill(@RequestBody UpdateBillsDTO updateBillsDTO) {
        return billsService.updateBills(updateBillsDTO);
    }

    @ApiOperation("逻辑删除账单")
    @PostMapping("/logic-delete")
    public R<String> logicDeleteBills(@RequestBody DeleteBillsDTO deleteBillsDTO) {
        return billsService.logicDeleteBills(deleteBillsDTO);
    }

    @ApiOperation("恢复被逻辑删除的账单")
    @PostMapping("/recover")
    public R<String> recoverBills(@RequestBody DeleteBillsDTO deleteBillsDTO) {
        return billsService.recoverBills(deleteBillsDTO);
    }

    @ApiOperation("查询被逻辑删除的账单")
    @PostMapping("/query-deleted-bills")
    public IPage<IsDeletedBillsVO> queryIsDeletedBills(@RequestBody PageDTO pageDTO) {
        return billsService.queryIsDeletedBills(pageDTO);
    }


    @ApiOperation("彻底删除账单")
    @PostMapping("/real-delete")
    public R<String> realDeleteBills(@RequestBody DeleteBillsDTO deleteBillsDTO) {
        return billsService.realDeleteBills(deleteBillsDTO);
    }
}
