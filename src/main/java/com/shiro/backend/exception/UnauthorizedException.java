package com.shiro.backend.exception;

/**
 * 自定义业务异常：用户未授权异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String msg) {
        super(msg, 50393);
    }

    public UnauthorizedException(String msg, Exception e) {
        super(msg, e, 50394);
    }
}
