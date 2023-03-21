package com.blackdragon.heytossme.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class ExceptionManager {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ExceptionResponse> memberExceptionHandler(final MemberException e) {

        log.info("Exception :" + e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(new ExceptionResponse(e.getMessage()));
    }
}
