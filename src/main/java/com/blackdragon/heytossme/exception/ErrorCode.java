package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements BaseErrorCodeImpl {
    // 해당 클래스는 삭제 예정이오니 해당 코드를 사용하시는 분은 refactoring 부탁드립니다.

    //토큰
    INCORRECT_KEY(HttpStatus.NOT_ACCEPTABLE, "Incorrect key"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Expired access token supplied"),
    //회원 가입에러
    CONFLICT_EMAIL(HttpStatus.CONFLICT, "Already existed email"),
    INCORRECT_AUTH_CODE(HttpStatus.CONFLICT, "Incorrect authorize code"),
    //회원 정보
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "Incorrect current password"),
    DIFFERENT_PASSWORD_REQUIRED(HttpStatus.CONFLICT, "Different password required"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Logout - refresh token expired"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Refreshtoken not existed");


    final HttpStatus httpStatus;
    final String message;
}
