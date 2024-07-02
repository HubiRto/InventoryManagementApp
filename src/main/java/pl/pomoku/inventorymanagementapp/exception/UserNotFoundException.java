package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id: %s not found", id), HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String email) {
        super(String.format("User with email: %s not found", email), HttpStatus.NOT_FOUND);
    }
}
