package com.example.databasedesign.controller;

import com.example.databasedesign.mapper.TableMapper;
import com.example.databasedesign.vo.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
