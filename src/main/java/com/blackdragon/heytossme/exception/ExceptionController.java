package com.blackdragon.heytossme.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> exception(CustomException customException) {
        var data = new ResponseForm(customException.getMessage());
        return ResponseEntity.status(customException.getErrorCode().getHttpStatus()).body(data);
    }
}
