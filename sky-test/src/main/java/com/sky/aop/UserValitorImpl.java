package com.sky.aop;

/**
 * 包名称： com.sky.aop
 * 类名称：UserValitorImpl
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-08 21:50
 */
public class UserValitorImpl implements UserValidator{
    @Override
    public boolean validate(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            return false;
        }
        if (user.getAge() == null || user.getAge() < 0) {
            return false;
        }
        return true;
    }
}