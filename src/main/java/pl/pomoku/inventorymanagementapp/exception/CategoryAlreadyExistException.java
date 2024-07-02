package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistException extends AppException {
    public CategoryAlreadyExistException() {
        super("Category with this name already exist", HttpStatus.CONFLICT);
    }
}
