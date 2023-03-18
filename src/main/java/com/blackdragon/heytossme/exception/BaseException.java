package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final BaseErrorCodeImpl errorCode;
    private final HttpStatus httpStatus;

    public BaseException(BaseErrorCodeImpl errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
    }

}
