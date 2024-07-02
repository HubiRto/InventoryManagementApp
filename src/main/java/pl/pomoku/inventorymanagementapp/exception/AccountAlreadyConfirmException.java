package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class AccountAlreadyConfirmException extends AppException {
    public AccountAlreadyConfirmException() {
        super("Account already confirmed", HttpStatus.BAD_REQUEST);
    }
}
