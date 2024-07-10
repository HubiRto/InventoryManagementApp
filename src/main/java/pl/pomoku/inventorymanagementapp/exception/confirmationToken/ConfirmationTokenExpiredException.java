package pl.pomoku.inventorymanagementapp.exception.confirmationToken;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ConfirmationTokenExpiredException extends AppException {
    public ConfirmationTokenExpiredException() {
        super("Confirmation token expired", HttpStatus.BAD_REQUEST);
    }
}
