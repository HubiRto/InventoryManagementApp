package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends AppException {
    public CategoryNotFoundException(Long id) {
        super(String.format("Category with id: %s not found", id), HttpStatus.NOT_FOUND);
    }

    public CategoryNotFoundException(String name) {
        super(String.format("Category with name: %s not found", name), HttpStatus.NOT_FOUND);
    }

    public CategoryNotFoundException() {
        super("Category not found", HttpStatus.NOT_FOUND);
    }
}
