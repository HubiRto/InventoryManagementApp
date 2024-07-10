package pl.pomoku.inventorymanagementapp.exception.auth;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class TokenNotFoundException extends AppException {
    public TokenNotFoundException() {
        super("Token not found", HttpStatus.NOT_FOUND);
    }
}
