package com.cerberus.userservice.controller;

import com.cerberus.userservice.exception.UserNotFoundException;
import com.cerberus.userservice.exception.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleException(UserNotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        problemDetail.setTitle("Пользователь не найден");
        return problemDetail;
    }

    @ExceptionHandler(UserValidationException.class)
    public ProblemDetail handleException(UserValidationException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage()
        );
        problemDetail.setTitle("Ошибка валидации");
        return problemDetail;
    }
}
