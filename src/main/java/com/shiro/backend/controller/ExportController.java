package com.shiro.backend.controller;

import com.shiro.backend.domain.dto.QueryMonthBillsDTO;
import com.shiro.backend.domain.vo.QueryBillsVO;
import com.shiro.backend.service.IBillsService;
import com.shiro.backend.utils.ExcelExportUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Api(tags = "数据导出接口")
@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Slf4j // 添加日志支持
public class ExportController {

    private final IBillsService billsService;

    @ApiOperation("导出指定月份账单数据")
    @GetMapping("/bills/monthly")
    public ResponseEntity<byte[]> exportMonthlyBills(@RequestParam int year, @RequestParam int month) {
        log.info("开始导出账单 - 年份: {}, 月份: {}", year, month);

        try {
            // 查询指定月份的账单数据
            QueryMonthBillsDTO queryDTO = new QueryMonthBillsDTO();
            queryDTO.setYear(year);
            queryDTO.setMonth(month);
            List<QueryBillsVO> bills = billsService.queryBills(queryDTO);
            
            log.info("成功查询到账单数据 - 数量: {}", bills != null ? bills.size() : 0);
            if (bills == null || bills.isEmpty()) {
                log.warn("查询结果为空，导出的Excel将不包含数据行");
            } else {
                // 记录第一笔账单信息，帮助调试
                QueryBillsVO firstBill = bills.get(0);
                log.info("第一条账单信息: ID={}, 金额={}, 类型={}, 日期={}",
                        firstBill.getId(), firstBill.getAmount(),
                        firstBill.getType(), firstBill.getDate());
            }

            // 生成Excel文件
            log.info("开始生成Excel文件...");
            byte[] excelBytes = ExcelExportUtils.exportMonthlyBills(bills, year, month);
            log.info("Excel文件生成完成，大小: {} 字节", excelBytes.length);

            // 构建文件名
            String fileName = year + "年" + month + "月账单明细.xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            log.info("文件名: {}, URL编码后: {}", fileName, encodedFileName);

            // 构建响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            headers.setContentDispositionFormData("attachment", encodedFileName);


            log.info("导出完成，准备返回响应");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            log.error("导出Excel过程中发生错误", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiOperation("导出指定月份账单数据到本地文件系统")
    @GetMapping("/bills/monthly/local")
    public ResponseEntity<String> exportMonthlyBillsToLocal(@RequestParam int year, @RequestParam int month) {
        log.info("开始导出账单到本地文件 - 年份: {}, 月份: {}", year, month);

        try {
            // 查询指定月份的账单数据
            QueryMonthBillsDTO queryDTO = new QueryMonthBillsDTO();
            queryDTO.setYear(year);
            queryDTO.setMonth(month);
            List<QueryBillsVO> bills = billsService.queryBills(queryDTO);


            // 生成Excel文件
            byte[] excelBytes = ExcelExportUtils.exportMonthlyBills(bills, year, month);

            // 确定保存路径（这里使用用户的Downloads文件夹）
            String userHome = System.getProperty("user.home");
            String downloadDir = userHome + "/Downloads/";
            String fileName = year + "年" + month + "月账单明细.xlsx";
            String filePath = downloadDir + fileName;

            // 确保目录存在
            new java.io.File(downloadDir).mkdirs();

            // 写入文件
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath)) {
                fos.write(excelBytes);
            }

            log.info("Excel文件已保存至: {}", filePath);

            return ResponseEntity.ok("文件已成功保存到: " + filePath);
        } catch (Exception e) {
            log.error("导出Excel到本地文件过程中发生错误", e);
            return ResponseEntity.internalServerError().body("导出失败: " + e.getMessage());
        }
    }
}