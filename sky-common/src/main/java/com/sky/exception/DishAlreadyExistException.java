package com.sky.exception;

/**
 * 包名称： com.sky.exception
 * 类名称：DishAlreadyExistException
 * 类描述：TODO
 * 创建人：@author Rain_润
 * 创建时间：2026-05-15 21:33
 */
public class DishAlreadyExistException extends BaseException {
    public DishAlreadyExistException() {
    }

    public DishAlreadyExistException(String msg) {
        super(msg);
    }
}