package com.thoughtworks.rslist.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String INVALID_RSEVENT_REQUEST_PARAM_ERROR_MESSAGE = "invalid request param";
    private static final String INVALID_RSEVENT_INDEX_ERROR_MESSAGE = "invalid index";
    private static final String INVALID_RSEVENT_POST_PARAM_ERROR_MESSAGE = "invalid param";
    private static final String INVALID_USER_POST_PARAM_ERROR_MESSAGE = "invalid user";

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({InvalidRequestParamException.class, InvalidIndexException.class,
            InvalidPostParamException.class, InvalidUserParamException.class})
    public ResponseEntity exceptionHandler(Exception ex){
        CommonError commonError = new CommonError();
        String errorMessage = null;
        if (ex instanceof InvalidRequestParamException)
            errorMessage = INVALID_RSEVENT_REQUEST_PARAM_ERROR_MESSAGE;
        else if (ex instanceof InvalidIndexException)
            errorMessage = INVALID_RSEVENT_INDEX_ERROR_MESSAGE;
        else if (ex instanceof InvalidPostParamException)
            errorMessage = INVALID_RSEVENT_POST_PARAM_ERROR_MESSAGE;
        else if (ex instanceof InvalidUserParamException)
            errorMessage = INVALID_USER_POST_PARAM_ERROR_MESSAGE;
        commonError.setError(errorMessage);
        logger.error("Exception:{}: --- {}", ex.getClass(), errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }
}