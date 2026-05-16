package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 包名称： com.sky.annotation
 * 类名称：AutoFill
 * 类描述：自动填充字段注解
 * 创建人：@author Rain_润
 * 创建时间：2026-05-15 16:59
 */
//该注解用于标记需要自动填充字段的方法，通常用于插入或更新操作
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //指定数据库操作类型，默认为INSERT
    OperationType value() default OperationType.INSERT;
}