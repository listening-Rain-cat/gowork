package com.sky.aop;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 包名称： com.sky.aop
 * 类名称：User
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-08 21:49
 */
@Component
@Data
public class User {
    private String name;
    private Integer age;
}