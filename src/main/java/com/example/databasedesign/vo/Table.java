package com.example.databasedesign.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/26 16:09
 * @version: 1.0.0
 */
@Data
public class Table {
    private String tableSchema; //库名
    private String tableName; //表名
    private Date createDate; //表的创建时间
    private String tableComment; //表名备注
    private String engine; //引擎


    private String columnName; //字段
    private String columnType; //字段类型
    private String columnComment; //字段备注
}
