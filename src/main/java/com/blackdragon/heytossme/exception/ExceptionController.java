package com.blackdragon.heytossme.exception;

import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({CustomException.class})
    public ErrorResponse exception(final CustomException customException) {
        return ErrorResponse.builder(customException, customException.getErrorCode().httpStatus,
                customException.getErrorCode().message).build();
    }
}
