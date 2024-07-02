package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class ConfirmationTokenExpiredException extends AppException {
    public ConfirmationTokenExpiredException() {
        super("Confirmation token expired", HttpStatus.BAD_REQUEST);
    }
}
