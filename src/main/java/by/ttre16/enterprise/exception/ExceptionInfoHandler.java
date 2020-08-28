package by.ttre16.enterprise.exception;

import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation
        .MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

import static by.ttre16.enterprise.exception.ErrorType.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {
    private static final Logger log = getLogger(ExceptionInfoHandler.class);

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFound(HttpServletRequest request,
            NotFoundException exception) {
        return logAndGetErrorInfo(request, exception, DATA_NOT_FOUND, false);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> dataIntegrityViolation(
            HttpServletRequest request,
            DataIntegrityViolationException exception) {
        return logAndGetErrorInfo(request, exception, VALIDATION_ERROR, true);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler({
            IllegalRequestDataException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorInfo> illegalRequestData(
            HttpServletRequest request, Exception exception) {
        return logAndGetErrorInfo(request, exception, VALIDATION_ERROR, false);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> internalServerError(
            HttpServletRequest request, Exception exception) {
        return logAndGetErrorInfo(request, exception, APP_ERROR, true);
    }

    private static ResponseEntity<ErrorInfo> logAndGetErrorInfo(
            HttpServletRequest request, Exception exception,
            ErrorType errorType, boolean isError) {
        String message = String.format(
                "%s -> at request '%s': %s",
                exception.getClass().getSimpleName(),
                request.getRequestURL(),
                exception.getMessage()
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
                        exception)
                );
    }
}
