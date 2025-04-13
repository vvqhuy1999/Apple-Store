package com.example.java5_asm_full;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestResultManager {
    private static final String FILE_PATH = "target/test-results.xlsx";
    private static final String SHEET_NAME = "Login Tests";
    private static List<Object[]> results = new ArrayList<>();
    private static boolean initialized = false;

    /**
     * Thêm kết quả test vào danh sách để lưu sau
     * @param testId ID của test case
     * @param stepNumber Số thứ tự của bước test
     * @param testDescription Mô tả test case
     * @param expectedResult Kết quả mong đợi
     * @param actualResult Kết quả thực tế
     * @param status Trạng thái (Pass/Fail)
     */
    public static void put(String testId, double stepNumber, String testDescription,
                           String expectedResult, String status) {
        results.add(new Object[]{
                testId,
                stepNumber,
                testDescription,
                expectedResult,
                status
        });
    }

    /**
     * Khởi tạo file Excel nếu chưa tồn tại
     */
    private static void initialize() {
        if (initialized) return;

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Tạo workbook mới nếu file không tồn tại
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet(SHEET_NAME);

                // Tạo header
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Test ID");
                headerRow.createCell(1).setCellValue("Step Number");
                headerRow.createCell(2).setCellValue("Test Description");
                headerRow.createCell(3).setCellValue("Expected Result");
                headerRow.createCell(4).setCellValue("Status");
                headerRow.createCell(5).setCellValue("Timestamp");

                // Format cột
                for (int i = 0; i < 6; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                    workbook.write(fileOut);
                }
            } catch (IOException e) {
                System.err.println("Error creating Excel file: " + e.getMessage());
            }
        }

        initialized = true;
    }

    /**
     * Lưu tất cả kết quả test vào file Excel
     */
    public static void saveResults() {
        initialize();

        try {
            // Đọc file hiện tại
            Workbook workbook;
            try (FileInputStream fileIn = new FileInputStream(FILE_PATH)) {
                workbook = new XSSFWorkbook(fileIn);
            }

            Sheet sheet = workbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                sheet = workbook.createSheet(SHEET_NAME);

                // Tạo header nếu sheet mới
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Test ID");
                headerRow.createCell(1).setCellValue("Step Number");
                headerRow.createCell(2).setCellValue("Test Description");
                headerRow.createCell(3).setCellValue("Expected Result");
                headerRow.createCell(4).setCellValue("Status");
                headerRow.createCell(5).setCellValue("Timestamp");
            }

            // Tìm hàng cuối cùng để thêm dữ liệu mới
            int lastRowNum = sheet.getLastRowNum();
            int rowStart = lastRowNum + 1;

            // Tạo cell style cho status
            CellStyle passStyle = workbook.createCellStyle();
            passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle failStyle = workbook.createCellStyle();
            failStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Lấy thời gian hiện tại
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Thêm các kết quả test
            for (int i = 0; i < results.size(); i++) {
                Object[] result = results.get(i);
                Row row = sheet.createRow(rowStart + i);

                // Thêm dữ liệu vào các cột
                Cell idCell = row.createCell(0);
                idCell.setCellValue((String) result[0]);

                Cell stepCell = row.createCell(1);
                stepCell.setCellValue((Double) result[1]);

                Cell descCell = row.createCell(2);
                descCell.setCellValue((String) result[2]);

                Cell expectedCell = row.createCell(3);
                expectedCell.setCellValue((String) result[3]);

                Cell statusCell = row.createCell(4);
                statusCell.setCellValue((String) result[4]);

                // Áp dụng màu dựa trên status
                if ("Pass".equals(result[4])) {
                    statusCell.setCellStyle(passStyle);
                } else {
                    statusCell.setCellStyle(failStyle);
                }

                Cell timeCell = row.createCell(5);
                timeCell.setCellValue(timestamp);
            }

            // Tự động điều chỉnh độ rộng cột
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            // Lưu workbook
            try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                workbook.write(fileOut);
                System.out.println("Test results saved to " + FILE_PATH);
            }

            // Đóng workbook
            workbook.close();

            // Xóa kết quả đã lưu
            results.clear();

        } catch (IOException e) {
            System.err.println("Error saving results to Excel: " + e.getMessage());
        }
    }
}