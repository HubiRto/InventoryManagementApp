package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class BillboardNotFoundException extends AppException {
    public BillboardNotFoundException(UUID id) {
        super(String.format("Billboard with id: %s not found", id), HttpStatus.NOT_FOUND);
    }
}
