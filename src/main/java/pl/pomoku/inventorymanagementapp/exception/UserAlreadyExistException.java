package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends AppException {
    public UserAlreadyExistException(String email) {
        super(String.format("User with email: %s, already exist", email), HttpStatus.BAD_REQUEST);
    }
}
