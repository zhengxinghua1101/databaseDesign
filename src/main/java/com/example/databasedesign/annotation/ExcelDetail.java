package com.example.databasedesign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Jeff.zheng
 * @description: 表格的细节
 * @date: 2023/10/28 8:50
 * @version: 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelDetail {

    public String name() default "";

    public int width() default 25; //默认是25 * 256

    public String readConverterExp() default "";  //读取内容转表达式 (如: 0=男,1=女,2=未知)

}
