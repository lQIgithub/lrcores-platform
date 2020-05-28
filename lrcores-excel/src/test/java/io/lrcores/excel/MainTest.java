package io.lrcores.excel;

import io.lrcores.excel.read.ExcelReaderUtils;
import io.lrcores.excel.write.ExcelWriterUtils;
import org.junit.Test;

import java.util.*;


public class MainTest {
    /**
     * excel表头
     *
     * @return
     */
    public static List<String> getHeaderList() {
        List<String> headerList = new LinkedList<>();
        //姓名	职业	居住城市	年龄
        headerList.add("姓名");
        headerList.add("职业");
        headerList.add("居住城市");
        headerList.add("年龄");
        return headerList;
    }

    /**
     * 读取excel表格数据
     * @throws Exception
     */
    @Test
    public void readExcel() throws Exception {
        List<String> headerList = MainTest.getHeaderList();
        String path = "D:\\testDirectory\\excel\\666.xlsx";
        /*传入参数说明
        1.带读取文件的路径
        2.表头名称*/
        ExcelReaderUtils.readExcel(path, headerList);
        List<Map<String, Object>> resultMapList = ExcelReaderUtils.resultMapList;
        for (Map map : resultMapList) {
            for (Object key : map.keySet()) {
                System.out.print(key + " : " + map.get(key) + "    ");
            }
            System.out.println("");
            System.out.println("=====================");
        }
    }


    @Test
    public void writeExcel() {
//         创建需要写入的数据列表
        List<Map<String, Object>> dataMapList = new ArrayList<>(2);
        Map<String, Object> dataMap = null;
        for (int i = 0; i <= 100000; i++) {
            dataMap = new HashMap<>();
            dataMap.put("name", "小明" + i);
            dataMap.put("age", 18);
            dataMap.put("location", "广州" + i);
            dataMap.put("job", "大学生" + i);
            dataMapList.add(dataMap);
        }

        /*表头标题栏  这里的设置需要注意两个事情
        第一：cellHeadMap的key 就是表头的名称
        第二：cellHeadMap的value就是数据源dataMapList中Map的key,需要一一对应*/
        List<Map<String, Object>> cellHeadMap = getCellHeadMapList();

//         写入数据到工作簿对象内
        ExcelWriterUtils.writeDataToExcel(dataMapList, cellHeadMap, "D:\\testDirectory\\excel\\666.xlsx");
    }


    /**
     * 生成excel的表头顺序
     * 使用linkedList保证excel写出的顺序
     * 表头标题栏  这里的设置需要注意两个事情
     * 第一：cellHeadMap的key 就是表头的名称
     * 第二：cellHeadMap的value就是数据源dataMapList中Map的key,需要一一对应
     *
     * @return
     */
    private List<Map<String, Object>> getCellHeadMapList() {
        List<Map<String, Object>> cellHeadMapList = new LinkedList<>();
        Map<String, Object> cellHeadMap = new HashMap<>();
        //企业名称	企业类型	行政区域编码	行政区域名称	组织机构代码	上一年总收入	上一年软件总收入	联系人	联系电话	固定电话	复工状态	复工方式	复工日期
        // 计划复工日期	企业人数	到岗已复工人数	远程复工数	未复工数	是否备案	备案时间	其它
        cellHeadMap.put("企业名称", "enterprise_name");
        cellHeadMapList.add(cellHeadMap);
        cellHeadMap = new HashMap<>();
        cellHeadMap.put("企业类型", "enterprise_type");
        cellHeadMapList.add(cellHeadMap);
        cellHeadMap = new HashMap<>();
        cellHeadMap.put("行政区域编码", "region_code");
        cellHeadMapList.add(cellHeadMap);
        //todo... excel表头有多少就在这里添加多少map，然后add到list中
        return cellHeadMapList;
    }


    @Test
    public void readExce3l() throws Exception {
        try {
            int i = 1;
            int i1 = i / 0;
            System.out.println(i1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
