package pl.pomoku.inventorymanagementapp.exception.auth;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class AccountAlreadyConfirmException extends AppException {
    public AccountAlreadyConfirmException() {
        super("Account already confirmed", HttpStatus.BAD_REQUEST);
    }
}
