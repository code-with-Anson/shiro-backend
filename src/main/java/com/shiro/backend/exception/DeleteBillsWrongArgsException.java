package com.shiro.backend.exception;

/**
 * 自定义业务异常：批量删除账单参数异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class DeleteBillsWrongArgsException extends BaseException {
    public DeleteBillsWrongArgsException(String msg) {
        super(msg, 50398);
    }
}
