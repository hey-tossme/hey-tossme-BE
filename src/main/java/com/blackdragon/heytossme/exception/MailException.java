package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;

@Getter
public class MailException extends BaseException {


    public MailException(BaseErrorCodeImpl errorCode) {
        super(errorCode);
    }
}
