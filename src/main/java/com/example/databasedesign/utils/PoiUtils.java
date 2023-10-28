package com.example.databasedesign.utils;

import cn.hutool.core.date.DateUtil;
import com.example.databasedesign.annotation.ExcelAttribute;
import com.example.databasedesign.annotation.ExcelDirectory;
import com.example.databasedesign.vo.Table;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/27 9:03
 * @version: 1.0.0
 */
public class PoiUtils {


    public static List<Table> getTableDistinctList(List<Table> list) {
//        List<String> tableNameList = list.stream().map(Table::getTableName).distinct().collect(Collectors.toList());
        List<Table> list1 = list.stream().map(item -> {
            Table table = new Table();
            table.setTableName(item.getTableName());
            table.setTableComment(item.getTableComment());
            table.setEngine(item.getEngine());
            table.setTableSchema(item.getTableSchema());
            table.setCreateDate(item.getCreateDate());
            return table;
        }).distinct().collect(Collectors.toList());
        return list1;
    }


    public static void addTableDirectory(Workbook workbook, List<Table> list) {
        String sheetName = "数据库设计目录";
        int countIndex = 0;
        boolean isUseSn = true;
        Class<Table> tableClass = Table.class;
        boolean isHadExcelAttribute = tableClass.isAnnotationPresent(ExcelAttribute.class);
        if (isHadExcelAttribute) {
            //含有
            ExcelAttribute annotation = tableClass.getAnnotation(ExcelAttribute.class);
            String tempSheetName = annotation.name();
            sheetName = tempSheetName;
            //使用序号
            if (!annotation.useSN()) {
                isUseSn = false;
            }
        }


        Field[] fields = tableClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelDirectory.class)) {
                countIndex++;
            }
        }


        Sheet directory = workbook.createSheet(sheetName);
        directory.setDefaultRowHeight((short) 500); // 系统默认是280左右
        directory.setDefaultColumnWidth(25);  //系统默认是8左右


        //第一行
        Row row1 = directory.createRow(0);
        row1.setHeightInPoints(35); //默认是16左右
        row1.createCell(0).setCellValue(sheetName);
        directory.addMergedRegion(new CellRangeAddress(0, 0, 0, isUseSn ? countIndex : countIndex - 1));

        //第二行
        Row row2 = directory.createRow(1);
        row2.setHeightInPoints(20); //默认是16左右
        int cell2Number = 0;
        if (isUseSn) {
            row2.createCell(cell2Number).setCellValue("序号");
            cell2Number++;
        }
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelDirectory.class)) {
                String name = field.getAnnotation(ExcelDirectory.class).name();
                row2.createCell(cell2Number).setCellValue(name);
                cell2Number++;
            }
        }

        //正文
        for (int i = 0; i < list.size(); i++) {
            Row temp = directory.createRow(2 + i);
            temp.setHeightInPoints(20); //默认是16左右
            Table table = list.get(i);
            int index = 0;
            if (isUseSn) {
                temp.createCell(index).setCellValue(i + 1);
                index++;
            }

            for (Field field : table.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelDirectory.class)) {
                    try {
                        field.setAccessible(true);
                        Cell tempCell = temp.createCell(index);
                        if (field.getType().toString().equals("class java.util.Date")) {
                            tempCell.setCellValue(DateUtil.format(DateUtil.parse(field.get(table).toString()), "yyyy-MM-dd HH:mm:ss"));
                        } else {
                            tempCell.setCellValue(field.get(table).toString());
                        }
                        //是否使用连接
                        if (field.getAnnotation(ExcelDirectory.class).useInk()) {
                            CreationHelper creationHelper = workbook.getCreationHelper();
                            Hyperlink link = creationHelper.createHyperlink(HyperlinkType.FILE);
                            String linkName = "#" + field.get(table).toString() + "!A1";
                            link.setAddress(linkName);  // #sheet2!A10跳转到sheet名称为sheet2的A10中去
                            tempCell.setHyperlink(link);
                        }
                        index++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static void addTableDetail(Workbook workbook, List<Table> list, Integer index) {

        String sheetName = list.get(0).getTableName();
        String tableComment = list.get(0).getTableComment();
        Sheet directory = workbook.createSheet(sheetName);
        // 超链接
        CreationHelper creationHelper = workbook.getCreationHelper();
        Hyperlink link = creationHelper.createHyperlink(HyperlinkType.FILE);

        directory.setDefaultRowHeight((short) 500); // 系统默认是280左右
        directory.setDefaultColumnWidth(25);  //系统默认是8左右

        Row row1 = directory.createRow(0);
        row1.setHeightInPoints(35); //默认是16左右
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue(sheetName);
        String linkName = "#数据库设计目录!B" + (2 + index);
        link.setAddress(linkName);  // #sheet2!A10跳转到sheet名称为sheet2的A10中去
        cell1.setHyperlink(link);
        directory.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        row1.createCell(3).setCellValue(tableComment);
        directory.addMergedRegion(new CellRangeAddress(0, 0, 3, 5));


        //第二行
        Row row2 = directory.createRow(1);
        row2.setHeightInPoints(20); //默认是16左右
        row2.createCell(0).setCellValue("序号");
        row2.createCell(1).setCellValue("字段名(中文)");
        row2.createCell(2).setCellValue("字段名(英文)");
        row2.createCell(3).setCellValue("类型");
        row2.createCell(4).setCellValue("大小");
        row2.createCell(5).setCellValue("备注");

        for (int i = 0; i < list.size(); i++) {
            Row temp = directory.createRow(2 + i);
            temp.setHeightInPoints(20); //默认是16左右
            temp.createCell(0).setCellValue(i + 1);
            temp.createCell(1).setCellValue(list.get(i).getColumnComment());
            temp.createCell(2).setCellValue(list.get(i).getColumnName());
            temp.createCell(3).setCellValue(list.get(i).getColumnType());
            temp.createCell(4).setCellValue(list.get(i).getColumnType());
            temp.createCell(5).setCellValue(list.get(i).getColumnComment());
        }


    }


    public static List<Table> initData() {
        List<Table> list = new ArrayList<>();
        Table table = new Table();
        table.setTableName("need_tabes");
        table.setColumnComment("需要的");
        table.setColumnName("table_name");
        table.setTableComment("表名");
        table.setColumnType("varchar(50)");
        table.setTableSchema("soft_cloud3");
        table.setEngine("InnoDB");
        table.setCreateDate(new Date());
        list.add(table);

        Table table1 = new Table();
        table1.setTableName("need_tabes");
        table1.setColumnComment("删除");
        table1.setColumnName("is_delete");
        table1.setTableComment("表名");
        table1.setColumnType("tinyint(1)");
        table1.setCreateDate(new Date());
        table1.setTableSchema("soft_cloud3");
        table1.setEngine("InnoDB");
        list.add(table1);

        Table table2 = new Table();
        table2.setTableName("need_tabes");
        table2.setColumnComment("主键");
        table2.setColumnName("id");
        table2.setTableComment("表名");
        table2.setColumnType("bigint");
        table2.setCreateDate(new Date());
        table2.setEngine("InnoDB");
        table2.setTableSchema("soft_cloud3");
        list.add(table2);

        Table table3 = new Table();
        table3.setTableName("t_user0");
        table3.setColumnComment("用户id");
        table3.setColumnName("user_id");
        table3.setTableComment("用户表");
        table2.setColumnType("bigint");
        table3.setCreateDate(new Date());
        table3.setEngine("InnoDB");
        table3.setTableSchema("soft_cloud3");
        list.add(table3);

        Table table4 = new Table();
        table4.setTableName("t_user0");
        table4.setColumnComment("标识");
        table4.setColumnName("remark");
        table4.setTableComment("用户表");
        table2.setColumnType("varchar(50)");
        table4.setCreateDate(new Date());
        table4.setEngine("InnoDB");
        table4.setTableSchema("soft_cloud3");
        list.add(table4);

        return list;
    }

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\Jeff.zheng.SOFTIDE\\Desktop\\test.xlsx");
        if (file.exists()) {
            file.delete();
            System.out.println("删除文件成功");
        }


        String path = "C:\\Users\\Jeff.zheng.SOFTIDE\\Desktop\\";

        List<Table> list = initData();

        Workbook workbook = new XSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        //主页
        List<Table> tableDistinctList = getTableDistinctList(list);
        addTableDirectory(workbook, tableDistinctList);


        //细节
        Integer index = 0;
        Map<String, List<Table>> tableMap = list.stream().collect(Collectors.groupingBy(Table::getTableName));
        for (List<Table> tableList : tableMap.values()) {
            index++;
            addTableDetail(workbook, tableList, index);
        }


        //设置所有样式（会覆盖原有的样式）
        for (Sheet rows : workbook) {
            for (Row cells : rows) {
                for (Cell cell1 : cells) {
                    cell1.setCellStyle(cellStyle);
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(path + "test.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("文件渡劫成功");

    }


}
