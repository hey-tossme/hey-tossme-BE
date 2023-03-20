package com.blackdragon.heytossme.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    // 해당 클래스는 삭제 예정이오니 해당 코드를 사용하시는 분은 refactoring 부탁드립니다.
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
