package com.sky.exception;

/**
 * 包名称： com.sky.exception
 * 类名称：CategoryNotFoundException
 * 类描述：分类不存在异常
 * 创建人：@author Rain_润
 * 创建时间：2026-05-14 16:38
 */
public class CategoryNotFoundException extends BaseException {
        public CategoryNotFoundException() {
        }

        public CategoryNotFoundException(String msg) {
            super(msg);
        }
}