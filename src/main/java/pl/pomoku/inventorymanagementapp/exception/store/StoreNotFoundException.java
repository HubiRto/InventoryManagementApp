package pl.pomoku.inventorymanagementapp.exception.store;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

import java.util.UUID;

public class StoreNotFoundException extends AppException {
    public StoreNotFoundException(Long id) {
        super(String.format("Store with id: %s not found", id), HttpStatus.NOT_FOUND);
    }
}
