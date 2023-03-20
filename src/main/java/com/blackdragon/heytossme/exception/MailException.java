package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.MailErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MailException extends RuntimeException{

    private final MailErrorCode mailErrorCode;
    private final HttpStatus httpStatus;

    public MailException(MailErrorCode mailErrorCode) {
        super(mailErrorCode.getMessage());
        this.mailErrorCode = mailErrorCode;
        this.httpStatus = mailErrorCode.getHttpStatus();
    }

}
