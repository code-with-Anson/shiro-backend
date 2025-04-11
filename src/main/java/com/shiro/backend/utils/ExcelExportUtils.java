package com.shiro.backend.utils;

import com.shiro.backend.domain.vo.QueryBillsVO;
import com.shiro.backend.enums.BillType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel导出工具类
 * 用于将账单数据导出为Excel文件
 */
@Slf4j
public class ExcelExportUtils {

    /**
     * 导出月度账单数据到Excel
     *
     * @param billsList 账单数据列表
     * @param year      年份
     * @param month     月份
     * @return Excel文件的字节数组
     * @throws IOException 如果导出过程中发生IO错误
     */
    public static byte[] exportMonthlyBills(List<QueryBillsVO> billsList, int year, int month) throws IOException {
        log.info("开始创建Excel - 年份: {}, 月份: {}, 数据条数: {}",
                year, month, billsList != null ? billsList.size() : 0);

        // 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // 创建工作表
            String sheetName = year + "年" + month + "月账单";
            Sheet sheet = workbook.createSheet(sheetName);
            log.info("已创建工作表: {}", sheetName);

            // 设置列宽
            sheet.setColumnWidth(0, 3000);  // 日期列
            sheet.setColumnWidth(1, 3500);  // 金额列
            sheet.setColumnWidth(2, 3000);  // 类型列
            sheet.setColumnWidth(3, 4000);  // 分类列
            sheet.setColumnWidth(4, 8000);  // 详情列

            // 创建样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle amountStyle = createAmountStyle(workbook);

            // 创建表头行
            Row headerRow = sheet.createRow(0);
            createHeaderRow(headerRow, headerStyle);

            // 处理账单数据为空的情况
            if (billsList == null || billsList.isEmpty()) {
                log.warn("没有账单数据可填充到Excel中");
                // 添加空白数据提示行
                Row noDataRow = sheet.createRow(1);
                Cell noDataCell = noDataRow.createCell(0);
                noDataCell.setCellValue("该月份没有账单数据");
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 4));
            } else {
                // 填充数据行
                fillDataRows(sheet, billsList, dateStyle, amountStyle);

                // 添加汇总行
                addSummaryRow(sheet, billsList, amountStyle);
            }

            // 将工作簿写入字节数组
            workbook.write(outputStream);
            outputStream.flush();
            byte[] result = outputStream.toByteArray();
            log.info("Excel导出完成，生成了{}字节的数据", result.length);

            return result;
        } catch (Exception e) {
            log.error("Excel导出过程中发生异常", e);
            throw new IOException("导出Excel失败: " + e.getMessage(), e);
        } finally {
            try {
                // 确保资源正确关闭
                workbook.close();
                outputStream.close();

            } catch (IOException e) {
                log.error("关闭资源时发生错误", e);
            }
        }
    }

    /**
     * 填充数据行
     */
    private static void fillDataRows(Sheet sheet, List<QueryBillsVO> billsList,
                                     CellStyle dateStyle, CellStyle amountStyle) {
        int rowNum = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (QueryBillsVO bill : billsList) {
            try {
                Row row = sheet.createRow(rowNum++);

                // 日期
                Cell dateCell = row.createCell(0);
                dateCell.setCellValue(bill.getDate() != null ? bill.getDate().format(formatter) : "");
                dateCell.setCellStyle(dateStyle);

                // 金额
                Cell amountCell = row.createCell(1);
                if (bill.getAmount() != null) {
                    amountCell.setCellValue(bill.getAmount().doubleValue());
                } else {
                    amountCell.setCellValue(0.0);
                }
                amountCell.setCellStyle(amountStyle);

                // 类型
                Cell typeCell = row.createCell(2);
                typeCell.setCellValue(bill.getType() == BillType.INCOME ? "收入" : "支出");

                // 分类ID
                Cell categoryCell = row.createCell(3);
                categoryCell.setCellValue("分类ID: " + bill.getCategoryId());

                // 详情
                Cell detailCell = row.createCell(4);
                detailCell.setCellValue(bill.getDetail() != null ? bill.getDetail() : "");
            } catch (Exception e) {
                log.error("处理第{}行数据时出错: {}", rowNum - 1, e.getMessage());
                // 继续处理下一行，不中断整个过程
            }
        }
        log.info("数据行填充完成，共处理{}行", rowNum - 1);
    }

    /**
     * 创建表头样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    /**
     * 创建日期样式
     */
    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    /**
     * 创建金额样式
     */
    private static CellStyle createAmountStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);

        // 设置货币格式
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));
        return style;
    }

    /**
     * 创建表头行
     */
    private static void createHeaderRow(Row headerRow, CellStyle headerStyle) {
        String[] headers = {"日期", "金额", "类型", "分类", "详情"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * 添加汇总行
     */
    private static void addSummaryRow(Sheet sheet, List<QueryBillsVO> billsList, CellStyle amountStyle) {
        int lastRowNum = sheet.getLastRowNum() + 2;  // 空出一行

        // 计算总收入和支出
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (QueryBillsVO bill : billsList) {
            if (bill.getAmount() == null) continue;

            if (bill.getType() == BillType.INCOME) {
                totalIncome = totalIncome.add(bill.getAmount());
            } else if (bill.getType() == BillType.EXPENSE) {
                totalExpense = totalExpense.add(bill.getAmount());
            }
        }

        // 计算结余
        BigDecimal balance = totalIncome.subtract(totalExpense);
        log.info("总收入: {}, 总支出: {}, 结余: {}", totalIncome, totalExpense, balance);

        // 创建汇总标题行
        Row titleRow = sheet.createRow(lastRowNum);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("月度汇总");

        Font boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);
        CellStyle boldStyle = sheet.getWorkbook().createCellStyle();
        boldStyle.setFont(boldFont);
        titleCell.setCellStyle(boldStyle);

        // 收入行
        Row incomeRow = sheet.createRow(lastRowNum + 1);
        incomeRow.createCell(0).setCellValue("总收入:");
        Cell incomeValueCell = incomeRow.createCell(1);
        incomeValueCell.setCellValue(totalIncome.doubleValue());
        incomeValueCell.setCellStyle(amountStyle);

        // 支出行
        Row expenseRow = sheet.createRow(lastRowNum + 2);
        expenseRow.createCell(0).setCellValue("总支出:");
        Cell expenseValueCell = expenseRow.createCell(1);
        expenseValueCell.setCellValue(totalExpense.doubleValue());
        expenseValueCell.setCellStyle(amountStyle);

        // 结余行
        Row balanceRow = sheet.createRow(lastRowNum + 3);
        balanceRow.createCell(0).setCellValue("月度结余:");
        Cell balanceValueCell = balanceRow.createCell(1);
        balanceValueCell.setCellValue(balance.doubleValue());
        balanceValueCell.setCellStyle(amountStyle);
    }
}