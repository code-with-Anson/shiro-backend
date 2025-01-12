package com.shiro.backend.handler;

import com.shiro.backend.constant.MessageConstant;
import com.shiro.backend.exception.BaseException;
import com.shiro.backend.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Object handleBadRequestException(BaseException e) {
        log.error("自定义异常 -> {} , 异常原因：{}  ", e.getClass().getName(), e.getMessage());
        log.debug("", e);
        return processResponse(e);
    }

    private ResponseEntity<R<Void>> processResponse(BaseException e) {
        return ResponseEntity.status(e.getCode()).body(R.failure(e));
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
