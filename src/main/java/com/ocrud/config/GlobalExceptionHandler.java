package com.ocrud.config;

import cn.hutool.core.lang.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @Resource
    private HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    public Dict handlerException(Exception ex) {
        log.info("异常定位：{}  异常信息：{}", ex.getStackTrace()[0].toString(),ex.getMessage());
        return Dict.create().set("code", 500)
                .set("msg", ex.getMessage())
                .set("path", request.getRequestURI());
    }
}
