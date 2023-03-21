package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MailErrorCode implements BaseErrorCodeImpl {

    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT,"Already exist Email"),
    INCORRECT_CODE(HttpStatus.UNAUTHORIZED, "Incorrect authorize code");

    private final HttpStatus httpStatus;
    private final String message;
}
