package io.lrcores.excel.write;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * excelWriter操作类
 */
@Slf4j
public class ExcelWriterUtils {

    /**
     * 第一步： 调用入口
     * 将数据库查询到的数据List<Map<String, Object>> dataMapList 写入到工作表中，返回工作表Workbook对象
     * 可以调用io流outputstream输出到浏览器
     * @param dataMapList 数据列表
     * @param cellHeadMapList 表头
     * @param fileName excel文件名称
     * @return 写入数据后的工作簿对象
     */
    public static Workbook writeDataToExcel(List<Map<String, Object>> dataMapList, List<Map<String, Object>> cellHeadMapList, String fileName) {
        // 生成xlsx的Excel
        Workbook workbook = createWorkbook(fileName);
        // 生成Sheet表，写入第一行的列头
        Sheet sheet = buildDataSheet(workbook, cellHeadMapList);
        //构建每行的数据内容
        int rowNum = 1;
        for (Map<String, Object> dataMap : dataMapList) {
            if (dataMap.isEmpty()) continue;
            //输出行数据
            Row row = sheet.createRow(rowNum++);
            convertDataToRow(dataMap, cellHeadMapList, row);
        }
        return workbook;
    }

    /**
     * 创建Workbook入口一 私有方法
     * 说明：调用次接口创建Workbook，假如文件已经存在，可以直接读取生成Workbook对象；
     * 假如文件不存在会调用“创建Workbook入口二”生成Workbook对象
     *
     * @param fileName 文件的全路径
     * @return
     */
    private static Workbook createWorkbook(String fileName) {
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(fileName);
            workbook = WorkbookFactory.create(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("****************要打开的文件不存在，正试图创建该文件，请稍后****************");
            //加入需要打开的文件不存在，则生成一个新的
            workbook = createWorkbookIfNotExist(fileName);
        } catch (Exception e) {
            log.error("****************文件打开异常****************");
            e.printStackTrace();
            throw new RuntimeException("文件打开异常");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("****************关闭输入流inputStream异常！****************");
                }
            }
        }
        return workbook;
    }

    /**
     * 创建Workbook入口二 私有方法
     * 说明：直接根据文件路径创建Workbook对象
     *
     * @param fileName 文件的全路径
     * @return
     */
    private static Workbook createWorkbookIfNotExist(String fileName) {
        Workbook workbook = null;
        if (fileName.endsWith(".xls")) {
            workbook = new HSSFWorkbook();
        } else if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook();
        } else {
            throw new RuntimeException("文件类型错误！请检查");
        }
        log.info("****************" + fileName + "创建成功!****************");
        return workbook;
    }


    /**
     * 生成sheet表，并写入第一行数据（列头）
     *
     * @param workbook        工作簿对象
     * @param cellHeadMapList 表头
     * @return 已经写入列头的Sheet
     */
    private static Sheet buildDataSheet(Workbook workbook, List<Map<String, Object>> cellHeadMapList) {
        Sheet sheet = workbook.createSheet();
        // 设置列头宽度
        for (int i = 0; i < cellHeadMapList.size(); i++) {
            sheet.setColumnWidth(i, 4000);
        }
        // 设置默认行高
        sheet.setDefaultRowHeight((short) 400);
        // 构建头单元格样式
        CellStyle cellStyle = buildHeadCellStyle(sheet.getWorkbook());
        // 写入第一行各列的数据
        Row rowHead = sheet.createRow(0);
        //循环设置表头
        int cellNum = 0;
        Cell cell;
        for (int i = 0; i < cellHeadMapList.size(); i++) {
            Map<String, Object> cellHeadMap = cellHeadMapList.get(i);
            for (String key : cellHeadMap.keySet()) {
                cell = rowHead.createCell(cellNum++);
                cell.setCellValue(key.trim());
                cell.setCellStyle(cellStyle);
            }
        }
        return sheet;
    }


    /**
     * 设置第一行列头的样式
     *
     * @param workbook 工作簿对象
     * @return 单元格样式对象
     */
    private static CellStyle buildHeadCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //对齐方式设置
        style.setAlignment(HorizontalAlignment.CENTER);
        //边框颜色和宽度设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 下边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边框
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边框
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // 上边框
        //设置背景颜色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //粗体字设置
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 将数据转换成行,保证写入顺序
     *
     * @param dataMap         源数据
     * @param cellHeadMapList 表头
     * @param row             行对象
     * @return
     */
    private static void convertDataToRow(Map<String, Object> dataMap, List<Map<String, Object>> cellHeadMapList, Row row) {
        int cellNum = 0;
        Cell cell;
        for (int i = 0; i < cellHeadMapList.size(); i++) {
            Map<String, Object> cellHeadMap = cellHeadMapList.get(i);
            for (String cellHead : cellHeadMap.keySet()) {
                for (String key : dataMap.keySet()) {
                    //log.info("key:" + key + ">>>cellHead:" + cellHead + ">>>cellHeadValue:" + cellHeadMap.get(cellHead).toString() + ">>>" + key.equalsIgnoreCase(cellHeadMap.get(cellHead).toString()));
                    if (key.equalsIgnoreCase(cellHeadMap.get(cellHead).toString())) {
                        cell = row.createCell(cellNum++);
                        cell.setCellValue(String.valueOf(dataMap.get(key)).trim());
                    }
                }
            }
        }
    }
}
