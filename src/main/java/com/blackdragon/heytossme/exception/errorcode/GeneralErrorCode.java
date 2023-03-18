package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeneralErrorCode implements BaseErrorCodeImpl {
    INSUFFICIENT_PARAMETER(HttpStatus.BAD_REQUEST, "Not sufficient parameter"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred");


    private final HttpStatus httpStatus;
    private final String message;
}
