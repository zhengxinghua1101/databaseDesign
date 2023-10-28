package com.example.databasedesign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Jeff.zheng
 * @description: 首页主页
 * @date: 2023/10/28 8:46
 * @version: 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelDirectory {

    public String name() default ""; //名称

    public double width() default 16; //宽度

    public boolean useInk() default false; //默认超链接
}
