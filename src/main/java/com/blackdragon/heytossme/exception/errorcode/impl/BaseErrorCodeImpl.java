package com.blackdragon.heytossme.exception.errorcode.impl;

import org.springframework.http.HttpStatus;

public interface BaseErrorCodeImpl {

    HttpStatus getHttpStatus();

    String getMessage();
}
