package com.shiro.backend.exception;

/**
 * 自定义业务异常：分类不存在异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class CategoryDontExistException extends BaseException {
    public CategoryDontExistException(String msg) {
        super(msg, 50397);
    }
}
