package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends AppException {
    public ProductNotFoundException(Long id) {
        super(String.format("Category with id: %s not found", id), HttpStatus.NOT_FOUND);
    }
}
