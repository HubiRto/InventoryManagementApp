package pl.pomoku.inventorymanagementapp.exception.user;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(Long id) {
        super("User with id: %d does not exist".formatted(id), HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String email) {
        super("User with email: %s does not exist".formatted(email), HttpStatus.NOT_FOUND);
    }
}
