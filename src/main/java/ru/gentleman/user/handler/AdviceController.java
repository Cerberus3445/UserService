package ru.gentleman.user.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gentleman.commom.exception.AlreadyExistsException;
import ru.gentleman.commom.exception.NotFoundException;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleException(NotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, this.messageSource.getMessage(
                        exception.getMessageKey(),
                        exception.getArgs(),
                        Locale.getDefault()
                )
        );
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.not_found",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ProblemDetail handleException(AlreadyExistsException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, this.messageSource.getMessage(
                        exception.getMessageKey(),
                        exception.getArgs(),
                        Locale.getDefault()
                )
        );
        problemDetail.setTitle(
                this.messageSource.getMessage(
                        "error.title.already_exist",
                        null,
                        Locale.getDefault()
                )
        );
        return problemDetail;
    }
}
