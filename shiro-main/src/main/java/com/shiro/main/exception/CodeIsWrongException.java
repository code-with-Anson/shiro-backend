package com.shiro.main.exception;

/**
 * 自定义业务异常：验证码错误异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class CodeIsWrongException extends BaseException {
    public CodeIsWrongException(String msg) {
        super(msg, 503910);
    }
}
