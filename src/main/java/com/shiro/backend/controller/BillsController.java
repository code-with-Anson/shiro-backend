package com.shiro.backend.controller;


import com.shiro.backend.domain.dto.BillsDTO;
import com.shiro.backend.service.IBillsService;
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
    public R<String> addNewBill(@RequestBody BillsDTO billsDTO) {
        billsService.saveBills(billsDTO);
        return R.success("成功添加！");
    }
}
