package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     *  Validation 실패 (MethodArgumentNotValidException)
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Validation Error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            String fileName = fieldError.getField().contains(".") ?
                    fieldError.getField().substring(fieldError.getField().lastIndexOf(".") + 1) :
                    fieldError.getField();
            errors.put(fileName, fieldError.getDefaultMessage());
        }
        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(
                        ErrorCode.BAD_REQUEST.getResultCode(),
                        "Validation failed",
                        errors
                ));
    }

    /**
     *  필수 요청 파라미터 누락 (MissingServletRequestParameterException)
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Missing Parameter: {}", ex.getMessage());

        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(
                        ErrorCode.BAD_REQUEST.getResultCode(),
                        "Missing parameter",
                        ex.getMessage()
                ));
    }


    @ExceptionHandler(PopcornException.class)
    public ResponseEntity<ApiResponse<String>> handlePopcornException(PopcornException ex) {
        return ResponseEntity.status(ex.getError().getResultCode())
                .body(ApiResponse.fail(ex.getError()));
    }


}
