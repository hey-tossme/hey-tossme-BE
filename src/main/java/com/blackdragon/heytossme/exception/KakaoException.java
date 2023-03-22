package com.blackdragon.heytossme.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KakaoException extends RuntimeException {

    private final KakaoErrorCode kakaoErrorCode;
    private final HttpStatus httpStatus;

    public KakaoException(KakaoErrorCode kakaoErrorCode) {
        super(kakaoErrorCode.getErrorCode());
        this.kakaoErrorCode = kakaoErrorCode;
        this.httpStatus = kakaoErrorCode.getHttpStatus();
    }
}
