package com.starluck.common;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author AI
 * @date 2026-06-01
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
