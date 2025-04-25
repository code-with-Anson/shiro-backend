package com.shiro.main.utils;

import com.shiro.main.domain.vo.QueryBillsVO;
import com.shiro.main.enums.BillType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public static byte[] exportMonthlyBills(List<QueryBillsVO> billsList, int year, Integer month) throws IOException {
        log.info("开始创建Excel - 年份: {}, 月份: {}, 数据条数: {}",
                year, month != null ? month : "全年", billsList != null ? billsList.size() : 0);

        // 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // 创建工作表
            String sheetName = month != null ?
                    year + "年" + month + "月账单" :
                    year + "年全年账单";
            Sheet sheet = workbook.createSheet(sheetName);

            // 设置列宽
            sheet.setColumnWidth(0, 3000);  // 日期列
            sheet.setColumnWidth(1, 3500);  // 金额列
            sheet.setColumnWidth(2, 3000);  // 类型列
            sheet.setColumnWidth(3, 4000);  // 分类列
            sheet.setColumnWidth(4, 8000);  // 详情列
            if (month == null) {
                sheet.setColumnWidth(5, 3000);  // 月份列
            }

            // 创建样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle amountStyle = createAmountStyle(workbook);

            // 处理账单数据为空的情况
            if (billsList == null || billsList.isEmpty()) {
                // 创建表头
                Row headerRow = sheet.createRow(0);
                if (month == null) {
                    createHeaderRowWithMonth(headerRow, headerStyle);
                } else {
                    createHeaderRow(headerRow, headerStyle);
                }

                // 创建无数据提示行
                Row noDataRow = sheet.createRow(1);
                Cell noDataCell = noDataRow.createCell(0);
                noDataCell.setCellValue(month != null ?
                        "该月份没有账单数据" :
                        "该年份没有账单数据");
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, month != null ? 4 : 5));
            } else {
                // 排序账单数据
                billsList = billsList.stream()
                        .sorted(Comparator.comparing(QueryBillsVO::getDate))
                        .collect(Collectors.toList());

                int currentRow = 0;

                // 先添加分类统计（显示在Excel最上方）
                currentRow = addCategoryStatistics(sheet, billsList, headerStyle, amountStyle, currentRow);

                // 空一行
                currentRow += 1;

                // 添加汇总行
                currentRow = addSummaryRow(sheet, billsList, headerStyle, amountStyle, currentRow, month == null);

                // 空一行
                currentRow += 1;

                // 添加表头
                Row headerRow = sheet.createRow(currentRow++);
                if (month == null) {
                    createHeaderRowWithMonth(headerRow, headerStyle);
                } else {
                    createHeaderRow(headerRow, headerStyle);
                }

                // 填充数据行
                if (month == null) {
                    fillDataRowsWithMonth(sheet, billsList, dateStyle, amountStyle, currentRow);
                } else {
                    fillDataRows(sheet, billsList, dateStyle, amountStyle, currentRow);
                }
            }

            // 写入字节数组
            workbook.write(outputStream);
            outputStream.flush();
            return outputStream.toByteArray();

        } finally {
            workbook.close();
            outputStream.close();
        }
    }

    /**
     * 填充数据行（不包含月份列）
     */
    private static void fillDataRows(Sheet sheet, List<QueryBillsVO> billsList,
                                     CellStyle dateStyle, CellStyle amountStyle, int startRowNum) {
        int rowNum = startRowNum;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (QueryBillsVO bill : billsList) {
            Row row = sheet.createRow(rowNum++);

            // 日期
            Cell dateCell = row.createCell(0);
            dateCell.setCellValue(bill.getDate() != null ? bill.getDate().format(formatter) : "");
            dateCell.setCellStyle(dateStyle);

            // 金额
            Cell amountCell = row.createCell(1);
            amountCell.setCellValue(bill.getAmount() != null ? bill.getAmount().doubleValue() : 0.0);
            amountCell.setCellStyle(amountStyle);

            // 类型
            Cell typeCell = row.createCell(2);
            typeCell.setCellValue(bill.getType() == BillType.INCOME ? "收入" : "支出");

            // 分类名称
            Cell categoryCell = row.createCell(3);
            categoryCell.setCellValue(bill.getCategoryName());

            // 详情
            Cell detailCell = row.createCell(4);
            detailCell.setCellValue(bill.getDetail() != null ? bill.getDetail() : "");
        }
    }

    /**
     * 填充包含月份的数据行
     */
    private static void fillDataRowsWithMonth(Sheet sheet, List<QueryBillsVO> billsList,
                                              CellStyle dateStyle, CellStyle amountStyle, int startRowNum) {
        int rowNum = startRowNum;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (QueryBillsVO bill : billsList) {
            Row row = sheet.createRow(rowNum++);

            // 日期
            Cell dateCell = row.createCell(0);
            dateCell.setCellValue(bill.getDate() != null ? bill.getDate().format(formatter) : "");
            dateCell.setCellStyle(dateStyle);

            // 金额
            Cell amountCell = row.createCell(1);
            amountCell.setCellValue(bill.getAmount() != null ? bill.getAmount().doubleValue() : 0.0);
            amountCell.setCellStyle(amountStyle);

            // 类型
            Cell typeCell = row.createCell(2);
            typeCell.setCellValue(bill.getType() == BillType.INCOME ? "收入" : "支出");

            // 分类名称
            Cell categoryCell = row.createCell(3);
            categoryCell.setCellValue(bill.getCategoryName());

            // 详情
            Cell detailCell = row.createCell(4);
            detailCell.setCellValue(bill.getDetail() != null ? bill.getDetail() : "");

            // 月份
            Cell monthCell = row.createCell(5);
            monthCell.setCellValue(bill.getDate() != null ? bill.getDate().getMonthValue() + "月" : "");
        }
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
     * 创建包含月份的表头行
     */
    private static void createHeaderRowWithMonth(Row headerRow, CellStyle headerStyle) {
        String[] headers = {"日期", "金额", "类型", "分类", "详情", "月份"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
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
     *
     * @return 返回下一行的行号
     */
    private static int addSummaryRow(Sheet sheet, List<QueryBillsVO> billsList,
                                     CellStyle headerStyle, CellStyle amountStyle,
                                     int startRowNum, boolean isYearly) {
        int currentRow = startRowNum;

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
        Row titleRow = sheet.createRow(currentRow++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(isYearly ? "年度汇总" : "月度汇总");
        titleCell.setCellStyle(headerStyle);

        // 收入行
        Row incomeRow = sheet.createRow(currentRow++);
        incomeRow.createCell(0).setCellValue("总收入:");
        Cell incomeValueCell = incomeRow.createCell(1);
        incomeValueCell.setCellValue(totalIncome.doubleValue());
        incomeValueCell.setCellStyle(amountStyle);

        // 支出行
        Row expenseRow = sheet.createRow(currentRow++);
        expenseRow.createCell(0).setCellValue("总支出:");
        Cell expenseValueCell = expenseRow.createCell(1);
        expenseValueCell.setCellValue(totalExpense.doubleValue());
        expenseValueCell.setCellStyle(amountStyle);

        // 结余行
        Row balanceRow = sheet.createRow(currentRow++);
        balanceRow.createCell(0).setCellValue(isYearly ? "年度结余:" : "月度结余:");
        Cell balanceValueCell = balanceRow.createCell(1);
        balanceValueCell.setCellValue(balance.doubleValue());
        balanceValueCell.setCellStyle(amountStyle);

        return currentRow;
    }

    /**
     * 单参数版本的汇总行添加方法（保留向后兼容）
     */
    private static void addSummaryRow(Sheet sheet, List<QueryBillsVO> billsList, CellStyle amountStyle) {
        addSummaryRow(sheet, billsList, createHeaderStyle(sheet.getWorkbook()), amountStyle, sheet.getLastRowNum() + 2, false);
    }

    /**
     * 添加分类统计
     *
     * @return 返回下一行的行号
     */
    private static int addCategoryStatistics(Sheet sheet, List<QueryBillsVO> billsList,
                                             CellStyle headerStyle, CellStyle amountStyle, int startRowNum) {
        int currentRow = startRowNum;

        // 创建分类统计标题
        Row titleRow = sheet.createRow(currentRow++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("分类统计");
        titleCell.setCellStyle(headerStyle);

        // 创建表头
        Row headerRow = sheet.createRow(currentRow++);
        String[] headers = {"分类名称", "收入金额", "支出金额", "结余"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 按分类分组统计
        Map<String, BigDecimal[]> categoryStats = new HashMap<>();

        for (QueryBillsVO bill : billsList) {
            if (bill.getAmount() == null || bill.getCategoryName() == null) continue;

            String categoryName = bill.getCategoryName();
            BigDecimal amount = bill.getAmount();

            // 获取或创建该分类的统计数组 [收入, 支出]
            BigDecimal[] stats = categoryStats.getOrDefault(categoryName,
                    new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});

            if (bill.getType() == BillType.INCOME) {
                stats[0] = stats[0].add(amount);  // 累加收入
            } else if (bill.getType() == BillType.EXPENSE) {
                stats[1] = stats[1].add(amount);  // 累加支出
            }

            categoryStats.put(categoryName, stats);
        }

        // 按分类名称排序
        List<Map.Entry<String, BigDecimal[]>> sortedEntries =
                new ArrayList<>(categoryStats.entrySet());
        sortedEntries.sort(Comparator.comparing(Map.Entry::getKey));

        // 填充分类统计数据
        for (Map.Entry<String, BigDecimal[]> entry : sortedEntries) {
            Row row = sheet.createRow(currentRow++);

            // 分类名称
            row.createCell(0).setCellValue(entry.getKey());

            // 收入金额
            Cell incomeCell = row.createCell(1);
            incomeCell.setCellValue(entry.getValue()[0].doubleValue());
            incomeCell.setCellStyle(amountStyle);

            // 支出金额
            Cell expenseCell = row.createCell(2);
            expenseCell.setCellValue(entry.getValue()[1].doubleValue());
            expenseCell.setCellStyle(amountStyle);

            // 结余
            Cell balanceCell = row.createCell(3);
            BigDecimal balance = entry.getValue()[0].subtract(entry.getValue()[1]);
            balanceCell.setCellValue(balance.doubleValue());
            balanceCell.setCellStyle(amountStyle);
        }

        return currentRow;
    }
}