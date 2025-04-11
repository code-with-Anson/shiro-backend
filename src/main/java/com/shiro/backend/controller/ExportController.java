package com.shiro.backend.controller;

import com.shiro.backend.domain.dto.QueryMonthBillsDTO;
import com.shiro.backend.domain.po.Users;
import com.shiro.backend.domain.vo.QueryBillsVO;
import com.shiro.backend.service.IBillsService;
import com.shiro.backend.service.IUsersService;
import com.shiro.backend.utils.EmailSender;
import com.shiro.backend.utils.ExcelExportUtils;
import com.shiro.backend.utils.R;
import com.shiro.backend.utils.UserContext;
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
import java.util.ArrayList;
import java.util.List;

@Api(tags = "数据导出接口")
@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Slf4j // 添加日志支持
public class ExportController {

    private final IBillsService billsService;
    private final IUsersService usersService; // 添加用户服务依赖
    private final EmailSender emailSender; // 添加邮件发送服务依赖

    @ApiOperation("导出账单数据(月度或年度)")
    @GetMapping("/bills/monthly")
    public ResponseEntity<byte[]> exportMonthlyBills(
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {
        log.info("开始导出账单 - 年份: {}, 月份: {}", year, month);

        try {
            List<QueryBillsVO> bills;
            String fileName;

            if (month == null) {
                // 导出全年账单
                bills = getYearlyBills(year);
                fileName = year + "年全年账单明细.xlsx";
                log.info("开始导出全年账单 - 年份: {}", year);
            } else {
                // 导出月度账单
                QueryMonthBillsDTO queryDTO = new QueryMonthBillsDTO();
                queryDTO.setYear(year);
                queryDTO.setMonth(month);
                bills = billsService.queryBills(queryDTO);
                fileName = year + "年" + month + "月账单明细.xlsx";
            }

            log.info("成功查询到账单数据 - 数量: {}", bills != null ? bills.size() : 0);

            // 生成Excel文件
            byte[] excelBytes = ExcelExportUtils.exportMonthlyBills(bills, year, month);

            // 构建文件名并编码
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replace("+", "%20");

            // 构建响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", encodedFileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            log.error("导出Excel过程中发生错误", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiOperation("导出账单数据到本地文件系统(月度或年度)")
    @GetMapping("/bills/monthly/local")
    public ResponseEntity<String> exportMonthlyBillsToLocal(
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {
        log.info("开始导出账单到本地文件 - 年份: {}, 月份: {}", year, month);

        try {
            List<QueryBillsVO> bills;
            String fileName;

            if (month == null) {
                // 导出全年账单
                bills = getYearlyBills(year);
                fileName = year + "年全年账单明细.xlsx";
                log.info("开始导出全年账单到本地文件 - 年份: {}", year);
            } else {
                // 导出月度账单
                QueryMonthBillsDTO queryDTO = new QueryMonthBillsDTO();
                queryDTO.setYear(year);
                queryDTO.setMonth(month);
                bills = billsService.queryBills(queryDTO);
                fileName = year + "年" + month + "月账单明细.xlsx";
            }

            // 生成Excel文件
            byte[] excelBytes = ExcelExportUtils.exportMonthlyBills(bills, year, month);

            // 确定保存路径（这里使用用户的桌面）
            String userHome = System.getProperty("user.home");
            String downloadDir = userHome + "/Desktop/";
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

    // 私有辅助方法，获取全年账单
    private List<QueryBillsVO> getYearlyBills(int year) {
        List<QueryBillsVO> allYearBills = new ArrayList<>();

        // 循环查询每个月的账单并合并
        for (int m = 1; m <= 12; m++) {
            QueryMonthBillsDTO queryDTO = new QueryMonthBillsDTO();
            queryDTO.setYear(year);
            queryDTO.setMonth(m);
            List<QueryBillsVO> monthBills = billsService.queryBills(queryDTO);
            if (monthBills != null && !monthBills.isEmpty()) {
                allYearBills.addAll(monthBills);
            }
        }

        return allYearBills;
    }

    @ApiOperation("导出账单数据并发送至用户邮箱")
    @GetMapping("/bills/monthly/email")
    public R<String> exportMonthlyBillsToEmail(
            @RequestParam int year,
            @RequestParam(required = false) Integer month) {
        log.info("开始导出账单并发送至用户邮箱 - 年份: {}, 月份: {}", year, month);

        try {
            // 获取当前用户ID
            Long userId = UserContext.getUser();

            // 通过用户服务获取用户邮箱
            Users user = usersService.getById(userId);
            if (user == null) {
                return R.failure("用户信息获取失败");
            }
            String userEmail = user.getEmail();

            // 准备数据和文件名
            List<QueryBillsVO> bills;
            String fileName;

            if (month == null) {
                // 导出全年账单
                bills = getYearlyBills(year);
                fileName = year + "年全年账单明细.xlsx";
            } else {
                // 导出月度账单
                QueryMonthBillsDTO queryDTO = new QueryMonthBillsDTO();
                queryDTO.setYear(year);
                queryDTO.setMonth(month);
                bills = billsService.queryBills(queryDTO);
                fileName = year + "年" + month + "月账单明细.xlsx";
            }

            // 生成Excel文件
            byte[] excelBytes = ExcelExportUtils.exportMonthlyBills(bills, year, month);

            // 设置邮件主题
            String subject = fileName.replace(".xlsx", "");

            // 发送邮件
            emailSender.sendExcelAttachment(userEmail, subject, excelBytes, fileName);

            return R.success("账单数据已成功发送到您的邮箱: " + userEmail);
        } catch (Exception e) {
            log.error("导出Excel并发送邮件过程中发生错误", e);
            return R.failure("导出失败: " + e.getMessage());
        }
    }
}