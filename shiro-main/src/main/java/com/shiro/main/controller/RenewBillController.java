package com.shiro.main.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiro.main.domain.dto.AddRenewBillDTO;
import com.shiro.main.domain.dto.DeleteRenewBillDTO;
import com.shiro.main.domain.dto.PageDTO;
import com.shiro.main.domain.dto.UpdateRenewBillDTO;
import com.shiro.main.domain.vo.IsDeletedRenewBillVO;
import com.shiro.main.domain.vo.QueryRenewBillVO;
import com.shiro.main.service.IRenewBillService;
import com.shiro.main.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 周期计费表，存储循环计费信息 前端控制器
 * </p>
 *
 * @author Anson
 * @since 2025-01-07
 */
@Api(tags = "循环账单接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/renew-bill")
public class RenewBillController {
    private final IRenewBillService renewBillService;

    @ApiOperation("记录循环账单")
    @PostMapping("/add")
    public R<String> addNewRenewBill(@RequestBody AddRenewBillDTO addRenewBillDTO) {
        return renewBillService.saveRenewBill(addRenewBillDTO);
    }

    @ApiOperation("分页查询循环账单")
    @PostMapping("/page-query")
    public IPage<QueryRenewBillVO> queryRenewBill(@RequestBody PageDTO pageDTO) {
        return renewBillService.queryRenewBill(pageDTO);
    }

    @ApiOperation("更新循环账单")
    @PostMapping("/update")
    public R<String> updateBill(@RequestBody UpdateRenewBillDTO updateRenewBillDTO) {
        return renewBillService.updateRenewBill(updateRenewBillDTO);
    }

    @ApiOperation("逻辑删除循环账单")
    @PostMapping("/logic-delete")
    public R<String> logicDeleteBills(@RequestBody DeleteRenewBillDTO deleteRenewBillDTO) {
        return renewBillService.logicDeleteRenewBill(deleteRenewBillDTO);
    }

    @ApiOperation("恢复被逻辑删除的循环账单")
    @PostMapping("/recover")
    public R<String> recoverBills(@RequestBody DeleteRenewBillDTO deleteRenewBillDTO) {
        return renewBillService.recoverRenewBill(deleteRenewBillDTO);
    }

    @ApiOperation("查询被逻辑删除的循环账单")
    @PostMapping("/query-deleted-bills")
    public IPage<IsDeletedRenewBillVO> queryIsDeletedBills(@RequestBody PageDTO pageDTO) {
        return renewBillService.queryIsDeletedRenewBill(pageDTO);
    }


    @ApiOperation("彻底删除循环账单")
    @PostMapping("/real-delete")
    public R<String> realDeleteBills(@RequestBody DeleteRenewBillDTO deleteRenewBillDTO) {
        return renewBillService.realDeleteRenewBill(deleteRenewBillDTO);
    }

}
