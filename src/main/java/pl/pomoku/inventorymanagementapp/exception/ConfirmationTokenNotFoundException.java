package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class ConfirmationTokenNotFoundException extends AppException {
    public ConfirmationTokenNotFoundException() {
        super("Confirmation token not found", HttpStatus.NOT_FOUND);
    }
}
