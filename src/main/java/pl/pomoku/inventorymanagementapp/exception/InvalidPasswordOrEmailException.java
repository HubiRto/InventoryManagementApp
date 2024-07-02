package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordOrEmailException extends AppException {
    public InvalidPasswordOrEmailException() {
        super("Invalid email or password", HttpStatus.BAD_REQUEST);
    }
}
