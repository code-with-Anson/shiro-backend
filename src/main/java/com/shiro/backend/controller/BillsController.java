package com.shiro.backend.controller;


import com.shiro.backend.domain.dto.AddBillsDTO;
import com.shiro.backend.domain.dto.DeleteBillsDTO;
import com.shiro.backend.domain.dto.QueryMonthBillsDTO;
import com.shiro.backend.domain.dto.UpdateBillsDTO;
import com.shiro.backend.domain.vo.IsDeletedBillsVO;
import com.shiro.backend.domain.vo.QueryMonthBillsVO;
import com.shiro.backend.service.IBillsService;
import com.shiro.backend.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 账单记录表 前端控制器
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Api(tags = "账单相关接口")
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
    public List<QueryMonthBillsVO> queryBills(@RequestBody QueryMonthBillsDTO queryMonthBillsDTO) {
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
    @GetMapping("/query-deleted-bills")
    public List<IsDeletedBillsVO> queryIsDeletedBills() {
        return billsService.queryIsDeletedBills();
    }


    @ApiOperation("彻底删除账单")
    @PostMapping("/real-delete")
    public R<String> realDeleteBills(@RequestBody DeleteBillsDTO deleteBillsDTO) {
        return billsService.realDeleteBills(deleteBillsDTO);
    }
}
