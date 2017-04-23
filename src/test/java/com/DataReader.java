package com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

class DataReader {

    static int rowSize, columnSize;

    static final private Log LOG = LogFactory.getLog(DataReader.class);

    String completeExcelData[][] = new String[0][];

    String[][] readExcel(int sheetNumber) throws IOException {
        String fileName = "DataSheet.xls"; //todo - from property file
        FileInputStream fis = new FileInputStream(fileName);
        HSSFSheet sheet = new HSSFWorkbook(new POIFSFileSystem(fis)).getSheetAt(sheetNumber);
        rowSize = sheet.getLastRowNum();
        columnSize = sheet.getRow(0).getLastCellNum();

        final String excelData[][] = new String[rowSize][columnSize];
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next(); //skipHeader

        rowLoop:
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getColumnIndex() == 1 && getValueFrom(cell).isEmpty()) continue rowLoop;
                excelData[row.getRowNum() - 1][cell.getColumnIndex()] = getValueFrom(cell);
            }
        }
        fis.close();
        return excelData;
    }

    String[][] readCompleteExcel() throws IOException {
        String fileName = "DataSheet.xls"; //todo - from property file
        FileInputStream fis = new FileInputStream(fileName);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new POIFSFileSystem(fis));
        for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); i++) {
            Sheet sheet = hssfWorkbook.getSheetAt(i);
            if (sheet.getLastRowNum() == 0) continue;
            if (getValueFrom(sheet.getRow(1).getCell(0)).isEmpty()) continue;
            completeExcelData = append(readExcel(sheet), completeExcelData);
        }
        fis.close();
        return completeExcelData;
    }

    String[][] readExcel(Sheet sheet) throws IOException {
        int rowSize = sheet.getLastRowNum();
        columnSize = sheet.getRow(0).getLastCellNum();
        this.rowSize += rowSize - 1;
        final String excelData[][] = new String[rowSize][columnSize];
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next(); //skipHeader

        rowLoop:
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getColumnIndex() == 1 && getValueFrom(cell).isEmpty()) continue rowLoop;
                excelData[row.getRowNum() - 1][cell.getColumnIndex()] = getValueFrom(cell);
            }
        }
        return excelData;
    }

    private static String[][] append(String[][] array1, String[][] array2) {
        String[][] result = new String[array1.length + array2.length][];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    private String getValueFrom(Cell cell) {
        if (cell == null) return "";
        String cellValue;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = String.valueOf((int) cell.getNumericCellValue());
                break;
            default:
                cellValue = cell.getStringCellValue();
                break;
        }
        return cellValue;
    }
}
