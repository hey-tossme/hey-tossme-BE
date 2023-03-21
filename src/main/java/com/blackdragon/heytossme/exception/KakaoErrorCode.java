package com.blackdragon.heytossme.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KakaoErrorCode {

    NOT_PARSED_JSON(HttpStatus.NOT_FOUND, "JSON 객체를 파싱 하지 못했습니다."),
    INCORRECT_REQUEST_URL(HttpStatus.NOT_FOUND, "잘못된 요청 주소 입니다.");



    private final HttpStatus httpStatus;
    private final String errorCode;

}
