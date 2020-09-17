package com.thoughtworks.rslist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String INVALID_REQUEST_PARAM_ERROR_MESSAGE = "invalid request param";
    private static final String INVALID_INDEX_ERROR_MESSAGE = "invalid index";
//    private static final String INVALID_REQUEST_PARAM_ERROR_MESSAGE = "invalid request param";

    @ExceptionHandler({InvalidRequestParamException.class, InvalidIndexException.class})
    public ResponseEntity exceptionHandler(Exception ex){
        CommonError commonError = new CommonError();
        String errorMessage = null;
        if (ex instanceof InvalidRequestParamException)
            errorMessage = INVALID_REQUEST_PARAM_ERROR_MESSAGE;
        else if (ex instanceof InvalidIndexException)
            errorMessage = INVALID_INDEX_ERROR_MESSAGE;
        commonError.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }
}