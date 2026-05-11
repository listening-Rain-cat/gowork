package com.sky.aop;

import org.springframework.stereotype.Service;

/**
 * 包名称： com.sky.aop
 * 类名称：UserService
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-08 20:02
 */
@Service
public class UserServiceImpl implements UserSerivce {
    @Override
    public void add() {
        System.out.println("添加用户");
    }

    @Override
    public void delete() {
        System.out.println("删除用户");
    }
}
