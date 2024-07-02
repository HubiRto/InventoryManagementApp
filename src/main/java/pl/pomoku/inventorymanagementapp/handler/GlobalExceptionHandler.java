package pl.pomoku.inventorymanagementapp.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pomoku.inventorymanagementapp.dto.response.ErrorResponse;
import pl.pomoku.inventorymanagementapp.exception.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> appExceptionHandler(AppException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.mapToErrorResponse());
    }
}
