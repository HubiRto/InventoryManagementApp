package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends AppException {
    public CategoryNotFoundException(Long id) {
        super(String.format("Category with id: %s not found", id), HttpStatus.NOT_FOUND);
    }
}
