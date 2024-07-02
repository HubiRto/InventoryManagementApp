package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends AppException {
    public TokenNotFoundException() {
        super("Token not found", HttpStatus.NOT_FOUND);
    }
}
