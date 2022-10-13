package com.cosmost.project.cosmost.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CourseControllerAdvice {

    // 코스 여부
    @ExceptionHandler(CourseIdNotfound.class)
    public ResponseEntity<String> CourseIdNotfoundException(CourseIdNotfound exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    // 쿼리 여부
    @ExceptionHandler(QueryNotfound.class)
    public ResponseEntity<String> QueryNotfoundException(QueryNotfound exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        FieldError fieldError = bindingResult.getFieldError();
        builder.append(fieldError.getDefaultMessage());

        return builder.toString();
    }



}
