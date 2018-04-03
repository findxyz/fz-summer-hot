package xyz.fz.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static List<Map<String, String>> readExcelToMaps(InputStream inputStream, int beginRowIndex, String[] columnKeys) throws IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(inputStream);
        Sheet sheet = wb.getSheetAt(0);
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = beginRowIndex; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            Map<String, String> map = new HashMap<>();
            for (int j = 0; j < columnKeys.length; j++) {
                String columnKey = columnKeys[j];
                Cell cell = row.getCell(j);
                map.put(columnKey, getCellValueAsString(cell));
            }
            list.add(map);
        }
        return list;
    }

    private static String getCellValueAsString(Cell cell) {
        String cellStringValue;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                cellStringValue = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    DateTime dateTime = new DateTime(cell.getDateCellValue());
                    cellStringValue = dateTime.toString("yyyy-MM-dd HH:mm:ss");
                } else {
                    cellStringValue = new DecimalFormat("0").format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                cellStringValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellStringValue = cell.getCellFormula();
                break;
            case BLANK:
                cellStringValue = "";
                break;
            default:
                cellStringValue = "";
        }
        return cellStringValue;
    }

    public static void writeMapsToExcel(OutputStream outputStream, String[] titles, String[] columnKeys, List<Map<String, String>> dataList) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("0");
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell titleCell = titleRow.createCell(i);
            titleCell.setCellValue(titles[i]);
        }
        for (int i = 0; i < dataList.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            Map<String, String> dataMap = dataList.get(i);
            for (int j = 0; j < columnKeys.length; j++) {
                String columnKey = columnKeys[j];
                Cell dataCell = dataRow.createCell(j);
                dataCell.setCellValue(dataMap.get(columnKey));
            }
        }
        wb.write(outputStream);
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {

        List<Map<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("productNo", "商品编号" + i);
            dataMap.put("price", Math.random() * 1000 + "");
            dataMap.put("remark", "一个备注" + i);
            dataList.add(dataMap);
        }

        String fileName = "test-" + System.currentTimeMillis() + ".xls";
        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        writeMapsToExcel(fileOutputStream, new String[]{"商品编号", "价格", "备注"}, new String[]{"productNo", "price", "remark"}, dataList);
        fileOutputStream.close();

        System.out.println(readExcelToMaps(new FileInputStream(file), 1, new String[]{"productNo", "remark", "remark"}));
    }
}
