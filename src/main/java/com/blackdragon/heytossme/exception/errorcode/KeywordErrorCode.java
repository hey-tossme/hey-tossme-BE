package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KeywordErrorCode implements BaseErrorCodeImpl {

    BOOKMARK_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Bookmark not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token supplied"),
    RESET_CONTENT(HttpStatus.RESET_CONTENT, "Reset Bookmark");

    private final HttpStatus httpStatus;
    private final String message;

}
