package pl.pomoku.inventorymanagementapp.exception.category;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class CategoryAlreadyExistException extends AppException {
    public CategoryAlreadyExistException() {
        super("Category with this name already exist", HttpStatus.CONFLICT);
    }
}
