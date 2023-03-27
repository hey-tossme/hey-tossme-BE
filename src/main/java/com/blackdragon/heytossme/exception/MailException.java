package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.MailErrorCode;
import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MailException extends BaseException {


    public MailException(BaseErrorCodeImpl errorCode) {
        super(errorCode);
    }
}
