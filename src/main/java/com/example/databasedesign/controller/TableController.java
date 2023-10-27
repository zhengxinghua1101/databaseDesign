package com.example.databasedesign.controller;

import com.example.databasedesign.mapper.TableMapper;
import com.example.databasedesign.vo.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

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

        String fileName = "123";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ServletOutputStream stream = response.getOutputStream();
//        stream.dagaishij


    }

}
