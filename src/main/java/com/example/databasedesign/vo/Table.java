package com.example.databasedesign.vo;

import com.example.databasedesign.annotation.ExcelAttribute;
import com.example.databasedesign.annotation.ExcelDetail;
import com.example.databasedesign.annotation.ExcelDirectory;
import lombok.Data;

import java.util.Date;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/26 16:09
 * @version: 1.0.0
 */
@Data
@ExcelAttribute(name = "数据库设计", SNWidth = 10 * 256, useSN = true)
public class Table {

    @ExcelDirectory(name = "表名(英文)", useInk = true)
    private String tableName; //表名

    @ExcelDirectory(name = "表名(中文)")
    private String tableComment; //表名备注

    @ExcelDirectory(name = "库名")
    private String tableSchema; //库名

    @ExcelDirectory(name = "引擎")
    private String engine; //引擎

    @ExcelDetail(name = "字段", width = 40 * 256)
    private String columnName; //字段

    @ExcelDetail(name = "字段类型", width = 20 * 256)
    private String dataType; //字段类型

    @ExcelDetail(name = "长度", width = 20 * 256)
    private Integer characterMaximumLength; //长度

    @ExcelDetail(name = "字段备注", width = 50 * 256)
    private String columnComment; //字段备注

    @ExcelDirectory(name = "创建时间")
    @ExcelDetail(name = "创建时间", width = 50 * 256)
    private Date createTime; //表的创建时间
}
