package com.swx.base.exception;

import com.swx.base.model.ErrorResult;
import com.swx.base.model.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * 全局异常处理类
 * @RestControllerAdvice(@ControllerAdvice)，拦截异常并统一处理
 * @author sw-code
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler.class");

    /**
     * 处理自定义的业务异常
     * @param e	异常对象
     * @param request	request
     * @return	错误结果
     */
    @ExceptionHandler(BizException.class)
    public ErrorResult bizExceptionHandler(BizException e, HttpServletRequest request) {
        logger.error("发生业务异常！原因是: {}", e.getMessage());
        return ErrorResult.fail(e.getCode(), e.getMessage());
    }

    // 拦截抛出的异常，@ResponseStatus：用来改变响应状态码
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResult handlerThrowable(Throwable e, HttpServletRequest request) {
        logger.error("发生未知异常！原因是: ", e);
        // 使用这种方式，而不是拦截AccessDeniedException异常，可引入Spring Security防止污染工程
        if (e.getMessage().equals("不允许访问")) {
            return ErrorResult.fail(ResultCodeEnum.NO_OPERATOR_AUTH, e);
        }
        return ErrorResult.fail(ResultCodeEnum.SERVER_ERROR, e);
    }

    // 参数校验异常
    @ExceptionHandler(BindException.class)
    public ErrorResult handleBindExcpetion(BindException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        // 存放错误信息
        String errMessage = getAllErrMessage(bindingResult);
        logger.error("发生参数校验异常！原因是：",e);
        return ErrorResult.fail(ResultCodeEnum.PARAM_INVALID, e, errMessage);
    }

    // 参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        String errMessage = getAllErrMessage(bindingResult);
        logger.error("发生参数校验异常！原因是：", e);
        return ErrorResult.fail(ResultCodeEnum.PARAM_INVALID, e, errMessage);
    }

    // 获取所有校验错误结果
    private static String getAllErrMessage(BindingResult bindingResult) {
        // 存放错误信息
        ArrayList<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(item -> {
            errors.add(item.getDefaultMessage());
        });
        return StringUtils.join(errors, ",");
    }
}

