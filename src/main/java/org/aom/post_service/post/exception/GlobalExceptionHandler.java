package org.aom.post_service.post.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.joining;

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
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(HandlerMethodValidationException e) {
        logger.info("GlobalExceptionHandler:handleValidationException() called with exception: {}", e.getMessage());
//        var errorList = e.getAllValidationResults()
//                .stream()
//                .map(ParameterValidationResult::getResolvableErrors)
//                .flatMap(List::stream)
//                .map(MessageSourceResolvable::getDefaultMessage)
//                .collect(HttpHeaders::new, (x, y) -> x.add("error", y), HttpHeaders::addAll);
        String errorMsg = e.getAllValidationResults()
                .stream()
                .map(ParameterValidationResult::getResolvableErrors)
                .flatMap(List::stream)
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(joining(", "));
        ApiError errObj = new ApiError(LocalDateTime.now(), errorMsg, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest()
                .body(errObj);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllOtherException(Exception ex){
        logger.info("GlobalExceptionHandler:handleAllOtherException() called with exception: {}", ex.getMessage());
        logger.info("GlobalExceptionHandler:handleAllOtherException() called with exception name : {}", ex.getClass().getName());
        ApiError errObj = new ApiError(LocalDateTime.now(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errObj);
    }
}
