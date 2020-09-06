package by.ttre16.enterprise.controller;

import by.ttre16.enterprise.exception.ErrorInfo;
import by.ttre16.enterprise.exception.ErrorType;
import by.ttre16.enterprise.exception.IllegalRequestDataException;
import by.ttre16.enterprise.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation
        .MethodArgumentTypeMismatchException;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import java.util.stream.Collectors;

import static by.ttre16.enterprise.exception.ErrorType.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionInfoHandler {
    private static final Logger log = getLogger(ExceptionInfoHandler.class);

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFound(HttpServletRequest request,
            NotFoundException exception) {
        return logAndGetErrorInfo(
                request,
                exception,
                DATA_NOT_FOUND,
                true,
                exception.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> dataIntegrityViolation(
            HttpServletRequest request,
            DataIntegrityViolationException exception) {
        return logAndGetErrorInfo(
                request,
                exception,
                VALIDATION_ERROR,
                true,
                exception.getMessage()
        );
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler({
            IllegalRequestDataException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorInfo> illegalRequestData(
            HttpServletRequest request, Exception exception) {
        exception.printStackTrace();
        return logAndGetErrorInfo(
                request,
                exception,
                VALIDATION_ERROR,
                false,
                exception.getMessage()
                );
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> internalServerError(
            HttpServletRequest request, Exception exception) {
        return logAndGetErrorInfo(
                request,
                exception,
                APP_ERROR,
                true,
                exception.getMessage()
        );
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorInfo> persistenceException(
            HttpServletRequest request,
            Exception exception) {
        String message = exception.getMessage();
        Throwable cause = exception.getCause();
        if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException constraintException =
                    (ConstraintViolationException) cause;
            message = constraintException.getConstraintViolations().stream()
                    .map(ex -> ex.getPropertyPath() + ": " + ex.getMessage())
                    .collect(Collectors.joining(", "));
        }
        return logAndGetErrorInfo(
                request,
                exception,
                VALIDATION_ERROR,
                true,
                message
        );
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ResponseEntity<ErrorInfo> methodArgumentNotValid(
            HttpServletRequest request,
            Exception exception) {
        BindingResult result = exception instanceof MethodArgumentNotValidException
                ? ((MethodArgumentNotValidException) exception).getBindingResult()
                : (BindingResult) exception;
        String message = result.getFieldErrors().stream()
                .map(error -> error.getField() + ": " +
                        (error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : error.getCode()))
                .collect(Collectors.joining(", "));
        return logAndGetErrorInfo(
                request,
                exception,
                VALIDATION_ERROR,
                true,
                message
        );
    }

    private ResponseEntity<ErrorInfo> logAndGetErrorInfo(
            HttpServletRequest request, Exception exception,
            ErrorType errorType, boolean isError, String details) {
        String message = String.format(
                "%s -> at request '%s': %s",
                exception.getClass().getSimpleName(),
                request.getRequestURL(),
                details
        );
        if (isError) {
            log.error(message);
        } else {
            log.warn(message);
        }
        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(
                        request.getRequestURL(),
                        errorType,
                        details)
                );
    }
}
