package io.lrcores.excel.read;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel读取工具，解决内存溢出
 * 第一步：获取方调用public static void readExcel(String fileName, List<String> headerList) throws Exception {}方法，
 * 第二步：通过此条语句 List<Map<String, Object>> resultMapList = ExcelReaderUtils.resultMapList;获取读取的信息，一行信息封装成一个Map对象
 **/
@Slf4j
public class ExcelReaderUtils {

    //excel2003扩展名
    private static final String EXCEL03_EXTENSION = ".xls";

    //excel2007扩展名
    private static final String EXCEL07_EXTENSION = ".xlsx";

    //用户自定义是否打开打印每行数据到控制台 默认false不打开
    private static boolean openPrint = false;

    //列表表头
    private static List<String> headerList = null;

    //excel所有数据的map集合
    public static List<Map<String, Object>> resultMapList = new ArrayList<>();

    //提供赋值操作，用户从调用时传入
    public static void setHeaderList(List<String> headerList, boolean openPrint) {
        ExcelReaderUtils.headerList = headerList;
        ExcelReaderUtils.openPrint = openPrint;
    }

    /**
     * 每获取一条记录，即打印
     * 在flume里每获取一条记录即发送，而不必缓存起来，可以大大减少内存的消耗，这里主要是针对flume读取大数据量excel来说的
     *
     * @param sheetName  每个sheet的名称
     * @param sheetIndex 每个sheet的索引
     * @param curRow     当前行
     * @param cellList   一行中的每个单元格数据
     */
    public static void sendRows(String filePath, String sheetName, int sheetIndex, int curRow, List<String> cellList) {

        //测试打印 用户可以通过参数openPrint关闭
        if (openPrint) {
            print(filePath, sheetName, sheetIndex, curRow, cellList);
        }

        //excel最终生成的map对象
        Map<String, Object> rowMap = new HashMap<>();
        for (int i = 0; i < headerList.size(); i++) {
            rowMap.put(headerList.get(i), cellList.get(i).trim());
        }
        resultMapList.add(rowMap);
    }


    /**
     * 第一步：读取excel文件入口一
     * 默认不打印每一行的数据日志，用户无法控制
     * @param fileName   带读取的文件
     * @param headerList 表头
     * @throws Exception
     */
    public static void readExcel(String fileName, List<String> headerList) throws Exception {
        readExcel(fileName, headerList, openPrint);
    }

    /**
     *
     * 第一步：读取excel文件入口二
     * 默认不打印每一行的数据日志，用户无法控制
     * @param file   带读取的文件
     * @param headerList 表头
     * @throws Exception
     */
    public static void readExcel(MultipartFile file, List<String> headerList) throws Exception {
        readExcel(file, headerList, openPrint);
    }

    /**
     * 第一步：读取excel文件入口二
     * 默认不打印每一行的数据日志，用户可以控制，通过设置openPrint
     * @param file   带读取的文件
     * @param headerList 表头
     * @param openPrint  用户控制是否需要在控制台打印每一行读取到的数据 ，系统默认false不打印
     * @throws Exception
     */
    public static void readExcel(MultipartFile file, List<String> headerList, boolean openPrint) throws Exception {
        setHeaderList(headerList, openPrint);//设置在静态表头
        int totalRows = 0;
        String fileName = file.getOriginalFilename();
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(EXCEL03_EXTENSION)) { //处理excel2003文件
            ExcelXlsReader excelXls = new ExcelXlsReader();
            totalRows = excelXls.process(file);
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {//处理excel2007文件
            ExcelXlsxReaderWithDefaultHandler excelXlsxReader = new ExcelXlsxReaderWithDefaultHandler();
            totalRows = excelXlsxReader.process(file);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
        log.info("excel的总行数：" + totalRows);
    }

    /**
     * 读取excel文件入口二
     * @param fileName 传入需要，需要全路径
     * @param headerList
     * @param openPrint
     * @throws Exception
     */
    public static void readExcel(String fileName, List<String> headerList, boolean openPrint) throws Exception {
        setHeaderList(headerList, openPrint);//设置在静态表头
        int totalRows = 0;
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(EXCEL03_EXTENSION)) { //处理excel2003文件
            ExcelXlsReader excelXls = new ExcelXlsReader();
            totalRows = excelXls.process(fileName);
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {//处理excel2007文件
            ExcelXlsxReaderWithDefaultHandler excelXlsxReader = new ExcelXlsxReaderWithDefaultHandler();
            totalRows = excelXlsxReader.process(fileName);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
        log.info("excel的总行数：" + totalRows);
    }

    /**
     * 用于打印读取到的每一行的数据 用户可以通过参数openPrint关闭
     *
     * @param filePath
     * @param sheetName
     * @param sheetIndex
     * @param curRow
     * @param cellList
     */
    private static void print(String filePath, String sheetName, int sheetIndex, int curRow, List<String> cellList) {
        /*这里是为了测试看结果才需要的*/
        StringBuffer oneLineSb = new StringBuffer();
        oneLineSb.append(filePath);
        oneLineSb.append("--");
        oneLineSb.append("sheet" + sheetIndex);
        oneLineSb.append("::" + sheetName);//加上sheet名
        oneLineSb.append("--");
        oneLineSb.append("row" + curRow);
        oneLineSb.append("::");
        for (String cell : cellList) {
            oneLineSb.append(cell.trim());
            oneLineSb.append("|");
        }
        String oneLine = oneLineSb.toString();
        if (oneLine.endsWith("|")) {
            oneLine = oneLine.substring(0, oneLine.lastIndexOf("|"));
        }// 去除最后一个分隔符
        log.info("一行的数据：" + oneLine);
        /*这里是为了测试看结果才需要的*/
    }
}
