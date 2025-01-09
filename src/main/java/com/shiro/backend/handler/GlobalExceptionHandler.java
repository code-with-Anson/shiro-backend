package com.shiro.backend.handler;

import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.exception.BaseException;
import com.shiro.backend.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理业务常规异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public R exceptionHandler(BaseException ex) {
        log.error("运行异常-具体异常：{}", ex.getMessage());
        return R.failure(ex.getMessage());
    }

    /**
     * 处理业务中的未定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public R handleOtherException(Exception ex) {
        log.error("系统出现未定义异常", ex);
        return R.failure(MessageConstant.UNKNOWN_ERROR);
    }


}
