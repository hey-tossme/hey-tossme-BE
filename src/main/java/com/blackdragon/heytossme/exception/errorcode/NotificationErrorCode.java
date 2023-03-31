package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements BaseErrorCodeImpl {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Notification not found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Notification - bad request");

    private final HttpStatus httpStatus;
    private final String message;
}
