package com.cn.fastcab.exception;

import com.cn.fastcab.config.ResponseCodeUtil;
import com.cn.fastcab.config.ResponseHandler;
import com.cn.fastcab.config.ResultUtil;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  全局异常处理
 * </p>
 *
 * @Author 伍繁长
 * @Date 2023/4/5 0005
 * @Version 1.0
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseHandler RuntimeExceptionHandler(RuntimeException e) {
        return ResultUtil.genFailResult(ResponseCodeUtil.FAIL.code(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseHandler MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map map = new HashMap();
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResultUtil.genFailResult(ResponseCodeUtil.FAIL.code(), "注册信息不符合要求", map);
    }

}
