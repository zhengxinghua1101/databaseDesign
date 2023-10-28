package com.example.databasedesign;

import cn.hutool.core.date.DateTime;
import com.example.databasedesign.mapper.TableMapper;
import com.example.databasedesign.utils.PoiUtils;
import com.example.databasedesign.vo.Table;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class DatabaseDesignApplicationTests {


    @Autowired
    TableMapper tableMapper;

    @Test
    void contextLoads() {
    }


    @Test
    void test1() throws Exception {

        File file = new File("C:\\Users\\Jeff.zheng.SOFTIDE\\Desktop\\test.xlsx");
        if (file.exists()) {
            file.delete();
            System.out.println("删除文件成功");
        }


        String path = "C:\\Users\\Jeff.zheng.SOFTIDE\\Desktop\\";
        //创建一个excel文件
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


        //第一个页面
        Sheet sheet = workbook.createSheet("sheet1");
        sheet.setDefaultRowHeight((short) 500); // 系统默认是280左右
        sheet.setDefaultColumnWidth(25);  //系统默认是8左右


        // 第一行
        Row row1 = sheet.createRow(0);
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("奥利给");
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue("给力奥");

        //第二行
        Row row2 = sheet.createRow(1);
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("超链接");

        // 超链接
        CreationHelper creationHelper = workbook.getCreationHelper();
        Hyperlink link = creationHelper.createHyperlink(HyperlinkType.FILE);
        link.setAddress("#sheet2!A10");  //跳转到sheet名称为sheet2的A10中去
        cell21.setHyperlink(link);


        Cell cell22 = row2.createCell(1);
        String s = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell22.setCellValue(s);

        Row row3 = sheet.createRow(2);
        Cell cell31 = row3.createCell(0);
        cell31.setCellValue("xxxx");

        Cell cell32 = row3.createCell(1);
        cell31.setCellValue("yyy");

        //第二个sheet
        Sheet sheet2 = workbook.createSheet("sheet2");
        // 合并单元格
        sheet2.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));


        Sheet sheet3 = workbook.createSheet("sheet3");
        sheet3.addMergedRegion(new CellRangeAddress(0, 2, 1, 2));

        workbook.createSheet("sheet4");
        workbook.createSheet("sheet5");

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
        System.out.println("文件生成完毕");
    }


    @Test
    void test2() throws Exception {
        File file = new File("C:\\Users\\Jeff.zheng.SOFTIDE\\Desktop\\test.xlsx");
        if (file.exists()) {
            file.delete();
            System.out.println("删除文件成功");
        }


        String path = "C:\\Users\\Jeff.zheng.SOFTIDE\\Desktop\\";

        List<com.example.databasedesign.vo.Table> list = tableMapper.selectTableByDataBaseName("xxl_job");

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
        List<com.example.databasedesign.vo.Table> tableDistinctList = PoiUtils.getTableDistinctList(list);
        PoiUtils.addTableDirectory(workbook, tableDistinctList);


        //细节
        Integer index = 0;
        Map<String, List<com.example.databasedesign.vo.Table>> tableMap = list.stream().collect(Collectors.groupingBy(com.example.databasedesign.vo.Table::getTableName));
        for (List<Table> tableList : tableMap.values()) {
            index++;
            PoiUtils.addTableDetail(workbook, tableList, index);
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
