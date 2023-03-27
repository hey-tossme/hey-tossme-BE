package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCodeImpl {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found"),
    INVALID_KEY(HttpStatus.UNAUTHORIZED, "Invalid token"),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "Invalid JWT signature"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Expired access token supplied"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Logout - refresh token expired"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "Incorrect current password"),
    INCORRECT_PATH(HttpStatus.BAD_REQUEST, "Incorrect logout api path"),
    MATCH_PREVIOUS_PASSWORD(HttpStatus.CONFLICT, "Different password required"),
    INCORRECT_CODE(HttpStatus.UNAUTHORIZED, "Incorrect authorize code");

    private final HttpStatus httpStatus;
    private final String message;
}
