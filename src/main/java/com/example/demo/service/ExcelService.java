package com.example.demo.service;

import com.example.demo.models.Stock;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public byte[] generateStockReport(List<Stock> stocks) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Unit Price", "Quantity", "Category Name", "Purchased Date", "Expired Date"};
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Create header cells
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // Set column width
            }

            // Create data rows
            int rowNum = 1;
            for (Stock stock : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getName());
                row.createCell(1).setCellValue(stock.getUnitPrice());
                row.createCell(2).setCellValue(stock.getQuantity());
                row.createCell(3).setCellValue(stock.getCategoryName());
                row.createCell(4).setCellValue(stock.getPurchasedDate());
                row.createCell(5).setCellValue(stock.getExpiredDate());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }


    }
}