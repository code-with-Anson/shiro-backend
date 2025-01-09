package com.shiro.backend.exception;

/**
 * 自定义基本异常类
 * 基于RuntimeException，扩展了返回信息属性
 * 之后如果有需求，可以加一个常量或者枚举类型的状态码
 * 这样就可以让前端自行判断错误类型并显示不同语言的报错
 * 以便于支持多语种业务场景
 */
public class BaseException extends RuntimeException {
    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }
}
