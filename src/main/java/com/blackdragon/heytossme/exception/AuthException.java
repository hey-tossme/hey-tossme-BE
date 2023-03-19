package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;

public class AuthException extends BaseException {

    public AuthException(BaseErrorCodeImpl errorCode) {
        super(errorCode);
    }
}
