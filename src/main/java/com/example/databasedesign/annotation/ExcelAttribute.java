package com.example.databasedesign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Jeff.zheng
 * @description: 表格属性
 * @date: 2023/10/28 8:46
 * @version: 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelAttribute {
    public String name() default ""; //默认名称

    public boolean useSN() default true; //默认使用序号

    public int SNWidth() default 25; //序列号的宽度默认是25 * 256

}
