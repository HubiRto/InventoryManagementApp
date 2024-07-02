package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class StoreAlreadyExistException extends AppException {
    public StoreAlreadyExistException() {
        super("Store with this name already exist", HttpStatus.CONFLICT);
    }
}
