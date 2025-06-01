package com.sky.exception;

/**
 * 业务异常
 */
public class BaseException extends RuntimeException {
    // 所有业务异常都继承BaseException

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
