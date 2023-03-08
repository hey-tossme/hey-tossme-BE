package com.blackdragon.heytossme.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INSUFFICIENT_PARAMETER(HttpStatus.BAD_REQUEST, "Not sufficient parameter");

    ErrorCode(HttpStatus statusCode, String message) {
        this.httpStatus = statusCode;
        this.message = message;
    }

    final HttpStatus httpStatus;
    final String message;
}
