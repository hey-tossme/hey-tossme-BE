package com.blackdragon.heytossme.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException{

    private final MemberErrorCode memberErrorCode;
    private final HttpStatus httpStatus;

    public MemberException(MemberErrorCode memberErrorCode) {
        super(memberErrorCode.getErrorCode());
        this.memberErrorCode = memberErrorCode;
        this.httpStatus = memberErrorCode.getHttpStatus();

    }

}
