package pl.pomoku.inventorymanagementapp.exception.auth;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class InvalidPasswordOrEmailException extends AppException {
    public InvalidPasswordOrEmailException() {
        super("Invalid email or password", HttpStatus.BAD_REQUEST);
    }
}
