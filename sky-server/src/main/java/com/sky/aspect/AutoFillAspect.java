package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 包名称： com.sky.aspect
 * 类名称：AutoFillAspect
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-15 17:06
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Before("@annotation(com.sky.annotation.AutoFill)")
    public void autoFill(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        //获取第一个参数，通常是需要自动填充的对象
        Object args = joinPoint.getArgs()[0];
        log.info("自动填充字段，参数：{}", args);
        //获得方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的注解
        AutoFill annotation = methodSignature.getMethod().getAnnotation(AutoFill.class);
        //获取注解中的值
        OperationType value = annotation.value();
        //根据操作类型进行不同的自动填充逻辑
        if (value == OperationType.INSERT) {
            //插入操作，自动填充创建时间和创建人等字段
            log.info("执行插入操作，自动填充创建时间和创建人等字段");
            //获得参数的字节码对象
            Class clazz = args.getClass();
            //通过反射获取需要自动填充的字段，并进行赋值
            Field createTimeField = clazz.getDeclaredField("createTime");
            createTimeField.setAccessible(true);
            Field createUserField = clazz.getDeclaredField("createUser");
            createUserField.setAccessible(true);
            Field updateTimeField = clazz.getDeclaredField("updateTime");
            updateTimeField.setAccessible(true);
            Field updateUserField = clazz.getDeclaredField("updateUser");
            updateUserField.setAccessible(true);
            //赋值
            createTimeField.set(args, LocalDateTime.now());
            updateTimeField.set(args, LocalDateTime.now());
            createUserField.set(args, BaseContext.getCurrentId());
            updateUserField.set(args, BaseContext.getCurrentId());
        } else if (value == OperationType.UPDATE) {
            //更新操作，自动填充更新时间和更新人等字段
            log.info("执行更新操作，自动填充更新时间和更新人等字段");
            Class clazz = args.getClass();
            Field updateTimeField = clazz.getDeclaredField("updateTime");
            Field updateUserField = clazz.getDeclaredField("updateUser");
            updateUserField.setAccessible(true);
            updateTimeField.setAccessible(true);
            updateTimeField.set(args, LocalDateTime.now());
            updateUserField.set(args, BaseContext.getCurrentId());
        }
    }
}