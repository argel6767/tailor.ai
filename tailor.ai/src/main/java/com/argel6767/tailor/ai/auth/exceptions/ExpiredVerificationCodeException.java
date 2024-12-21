package com.argel6767.tailor.ai.auth.exceptions;

public class ExpiredVerificationCodeException extends RuntimeException {
    public ExpiredVerificationCodeException(String message) {
        super(message);
    }
}
