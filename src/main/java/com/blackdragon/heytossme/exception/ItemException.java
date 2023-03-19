package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;

public class ItemException extends BaseException {

    public ItemException(BaseErrorCodeImpl errorCode) {
        super(errorCode);
    }
}
