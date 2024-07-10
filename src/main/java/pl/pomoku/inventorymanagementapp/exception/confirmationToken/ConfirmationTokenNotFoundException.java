package pl.pomoku.inventorymanagementapp.exception.confirmationToken;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ConfirmationTokenNotFoundException extends AppException {
    public ConfirmationTokenNotFoundException() {
        super("Confirmation token not found", HttpStatus.NOT_FOUND);
    }
}
