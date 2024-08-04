package com.chinmay.utils;


import com.chinmay.constants.FrameworkConstants;
import com.chinmay.exceptions.FrameworkTestException;
import com.chinmay.exceptions.InvalidFilePathException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class ExcelUtils {
    private ExcelUtils() {
        // Private constructor to prevent instantiation
    }

    public static List<ConcurrentHashMap<String, String>> getTestDetails(String sheetname) {
        List<ConcurrentHashMap<String, String>> list = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(FrameworkConstants.getExcelsheetspath() + "TestCases.xlsx")) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheet(sheetname);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                ConcurrentHashMap<String, String> concurrentMap = new ConcurrentHashMap<>();
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }

                    String key = sheet.getRow(0).getCell(j).getStringCellValue();
                    String value = cell.getCellType() == CellType.NUMERIC ? NumberToTextConverter.toText(cell.getNumericCellValue()) : cell.getStringCellValue();
                    concurrentMap.put(key, value);
                }

                list.add(concurrentMap);
            }
        } catch (FileNotFoundException e) {
            throw new InvalidFilePathException("Excel sheet you're trying to read is not found.");
        } catch (IOException e) {
            throw new FrameworkTestException("Problem occurred during an input-output operation.");
        }

        return list;
    }
}