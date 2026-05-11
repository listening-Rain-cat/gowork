package com.sky.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 包名称： com.sky.aop
 * 类名称：MyAspect
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-08 21:06
 */
@Aspect
@Component
public class MyAspect {
    //定义切点为UserSerivce接口的所有方法
    private  static final String AopExp = "execution(* com.sky.aop.UserSerivce.*(..))";
    @Pointcut(AopExp)
    public void pointCut1() {
    }

    @Before("pointCut1()")
    public void before1() {
        System.out.println("前置通知：方法执行前");
    }
    @After("pointCut1()")
    public void after() {
        System.out.println("后置通知：方法执行后");
    }
    @AfterReturning("pointCut1()")
    public void afterReturning() {
        System.out.println("返回通知：方法正常返回后");
    }
    @AfterThrowing("pointCut1()")
    public void afterThrowing() {
        System.out.println("异常通知：方法抛出异常后");
    }
    @Around("pointCut1()")
    public void around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕通知：方法执行前");
        // ProceedingJoinPoint.proceed()方法用于执行目标方法
        jp.proceed();
        System.out.println("环绕通知：方法执行后");
    }
}