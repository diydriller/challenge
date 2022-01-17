package com.project.programmers_order.errors;

public class FailException extends RuntimeException {
    public FailException(String message) {
        super(message);
    }
    public FailException(String message, Throwable cause) {
        super(message, cause);
    }
}
