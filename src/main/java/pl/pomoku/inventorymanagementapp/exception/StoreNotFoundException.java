package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class StoreNotFoundException extends AppException {
    public StoreNotFoundException(UUID id) {
        super(String.format("Store with id: %s not found", id), HttpStatus.NOT_FOUND);
    }
}
