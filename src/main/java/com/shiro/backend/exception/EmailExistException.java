package com.shiro.backend.exception;

/**
 * 自定义业务异常：邮箱已存在异常
 * 继承了我们自己定义的BaseExceptiong
 * 允许我们返回给前端异常信息
 */
public class EmailExistException extends BaseException {
    public EmailExistException() {
    }

    public EmailExistException(String msg) {
        super(msg);
    }
}
