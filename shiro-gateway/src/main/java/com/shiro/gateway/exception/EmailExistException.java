package com.shiro.gateway.exception;

/**
 * 自定义业务异常：邮箱已存在异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class EmailExistException extends BaseException {
    public EmailExistException(String msg) {
        super(msg, 50391);
    }
}
