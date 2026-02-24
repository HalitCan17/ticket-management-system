package com.halitcan.ticket_management_system.common.exception;

import com.halitcan.ticket_management_system.common.Trace;
import com.halitcan.ticket_management_system.common.api.ApiError;
import com.halitcan.ticket_management_system.common.api.ApiFieldError;
import com.halitcan.ticket_management_system.common.api.ApiResponse;
import com.halitcan.ticket_management_system.domain.exception.BaseBusinessException;
import com.halitcan.ticket_management_system.domain.exception.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    // 1. İş Mantığı Hataları
    @ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessExceptions(BaseBusinessException ex) {
        HttpStatus status = (ex instanceof NotFoundException) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

        ApiError error = ApiError.of(ex.getErrorCode(), ex.getMessage());
        log.warn("İş kuralı hatası: {} - traceId: {}", ex.getMessage(), Trace.id());

        return ResponseEntity.status(status).body(ApiResponse.fail(error));
    }

    // 2. Validasyon Hataları
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiFieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ApiFieldError(err.getField(), err.getDefaultMessage()))
                .toList();

        ApiError error = ApiError.validation(fieldErrors);
        log.warn("Validasyon hatası alındı - traceId: {}", Trace.id());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
    }

    // 3. Beklenmedik Sistem Hataları
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        ApiError error = ApiError.of("INTERNAL_ERROR", "Sistemde beklenmeyen bir teknik hata oluştu.");

        // Kritik hatalarda stack trace OpenSearch'e gitsin diye log.error
        log.error("KRİTİK HATA: traceId: {}", Trace.id(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(error));
    }
}