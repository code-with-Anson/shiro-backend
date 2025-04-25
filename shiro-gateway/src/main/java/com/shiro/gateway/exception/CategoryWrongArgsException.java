package com.shiro.gateway.exception;

/**
 * 自定义业务异常：分类参数异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class CategoryWrongArgsException extends BaseException {
    public CategoryWrongArgsException(String msg) {
        super(msg, 50396);
    }
}
