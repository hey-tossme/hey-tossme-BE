package com.blackdragon.heytossme.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {

    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "User not found"),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "Incorrect current password"),
    MATCH_PREVIOUS_PASSWORD(HttpStatus.CONFLICT, "Different password required");

    private final HttpStatus httpStatus;
    private final String errorCode;
}
