package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;

public class ChatException extends BaseException {

    public ChatException(BaseErrorCodeImpl errorCode) {
        super(errorCode);
    }
}
