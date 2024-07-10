package pl.pomoku.inventorymanagementapp.exception.category;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class CategoryNotFoundException extends AppException {
    public CategoryNotFoundException(Long id) {
        super(String.format("Category with id: %s not found", id), HttpStatus.NOT_FOUND);
    }
}
