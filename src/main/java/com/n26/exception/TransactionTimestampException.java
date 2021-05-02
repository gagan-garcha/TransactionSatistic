package com.n26.exception;

import org.springframework.http.HttpStatus;

public class TransactionTimestampException extends Exception {
    private final HttpStatus httpStatus;

    public TransactionTimestampException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
