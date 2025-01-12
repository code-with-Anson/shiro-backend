package com.shiro.backend.exception;

/**
 * 自定义业务异常：用户密码错误异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class PasswordErrorException extends BaseException {
    public PasswordErrorException(String msg) {
        super(msg, 50392);
    }
}
