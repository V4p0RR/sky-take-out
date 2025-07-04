package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * 
     * @param ex
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获sql异常
     * 
     * @param ex
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String username = split[2];
            String msg = "用户名:" + username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

    /**
     * 捕获修改密码异常
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler
    @SuppressWarnings("rawtypes")
    public Result exceptionHandler(PasswordEditFailedException ex) {
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获删除异常
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler
    @SuppressWarnings("rawtypes")
    public Result exceptionHandler(DeletionNotAllowedException ex) {
        return Result.error(ex.getMessage());
    }

}
