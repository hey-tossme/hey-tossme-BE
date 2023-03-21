package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.GeneralErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseForm> memberException(BaseException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ResponseForm(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseForm> validationException(
            MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseForm(GeneralErrorCode.INSUFFICIENT_PARAMETER.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseForm> exception(Exception e) {
        log.error("error message = {}", e.getMessage());
        for (var el : e.getStackTrace()) {
            log.error("error from = {}", el);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseForm(GeneralErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

}
