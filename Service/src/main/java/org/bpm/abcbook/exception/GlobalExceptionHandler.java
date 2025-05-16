package org.bpm.abcbook.exception;

import org.bpm.abcbook.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    public MessageSource messageSource;

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> runtimeExceptionHandler(RuntimeException e) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> appExeptionHandler(AppException e) {
        String errorCode = e.getCode();
        String errorMessage = e.getMessage();
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode)
                .message(messageSource.getMessage(errorMessage, null, errorMessage, LocaleContextHolder.getLocale()))
                .success(false)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        AtomicReference<String> msg = new AtomicReference<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> msg.set(err.getDefaultMessage()));
        String errorMsg = msg.get();
        ApiResponse apiResponse = ApiResponse.builder()
                .success(false)
                .code("HVE00000")
                .message(messageSource.getMessage(errorMsg, null, errorMsg, LocaleContextHolder.getLocale()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
