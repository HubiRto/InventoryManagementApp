package pl.pomoku.inventorymanagementapp.exception.category;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class CategoryCyclicDependencyException extends AppException {
    public CategoryCyclicDependencyException() {
        super("Cannot set parent. This creates a cyclic dependency.", HttpStatus.CONFLICT);
    }
}
