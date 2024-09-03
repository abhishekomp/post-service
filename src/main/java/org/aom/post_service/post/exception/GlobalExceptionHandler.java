package org.aom.post_service.post.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex){
        logger.info("GlobalExceptionHandler:handleUserNotFoundException() called with exception: {}", ex.getMessage());
        ApiError errObj = new ApiError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errObj);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiError> handlePostNotFoundException(PostNotFoundException ex){
        logger.info("GlobalExceptionHandler:handlePostNotFoundException() called with exception: {}", ex.getMessage());
        ApiError errObj = new ApiError(LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errObj);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllOtherException(Exception ex){
        logger.info("GlobalExceptionHandler:handleAllOtherException() called with exception: {}", ex.getMessage());
        logger.info("GlobalExceptionHandler:handleAllOtherException() called with exception name : {}", ex.getClass().getName());
        ApiError errObj = new ApiError(LocalDateTime.now(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errObj);
    }
}
