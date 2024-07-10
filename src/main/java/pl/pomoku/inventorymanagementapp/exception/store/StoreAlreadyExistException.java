package pl.pomoku.inventorymanagementapp.exception.store;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class StoreAlreadyExistException extends AppException {
    public StoreAlreadyExistException(String name) {
        super("Store with this name %s already exist".formatted(name), HttpStatus.CONFLICT);
    }
}
