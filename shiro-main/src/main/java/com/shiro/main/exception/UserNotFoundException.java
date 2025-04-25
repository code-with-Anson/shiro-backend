package com.shiro.main.exception;

/**
 * 自定义业务异常：用户不存在异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String msg) {
        super(msg, 50395);
    }
}
