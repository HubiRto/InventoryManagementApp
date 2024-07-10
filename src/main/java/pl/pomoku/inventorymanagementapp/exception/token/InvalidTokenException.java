package pl.pomoku.inventorymanagementapp.exception.token;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class InvalidTokenException extends AppException {
    public InvalidTokenException() {
        super("Invalid token", HttpStatus.BAD_REQUEST);
    }
}
