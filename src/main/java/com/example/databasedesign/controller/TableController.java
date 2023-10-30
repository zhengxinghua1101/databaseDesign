package com.example.databasedesign.controller;

import com.example.databasedesign.mapper.TableMapper;
import com.example.databasedesign.utils.PoiUtils;
import com.example.databasedesign.vo.Table;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/27 8:00
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TableController {

    private final TableMapper tableMapper;


    @GetMapping("/get")
    String getAll() {

        List<Table> tables = tableMapper.selectTableByDataBaseName("ds0");

        return tables.toString();
    }


    @GetMapping("/export")
    void export(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fileName = "数据库设计.xlsx";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        List<com.example.databasedesign.vo.Table> list = tableMapper.selectTableByDataBaseName("softide_cloud3");

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
        Map<String, List<Table>> tableMap = list.stream().collect(Collectors.groupingBy(com.example.databasedesign.vo.Table::getTableName));
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
        ServletOutputStream stream = response.getOutputStream();
        workbook.write(stream);
        workbook.close();
        stream.close();
        System.out.println("文件渡劫成功");

    }

}
