package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class ProductAlreadyExistException extends AppException {
    public ProductAlreadyExistException() {
        super("Product with this name already exist", HttpStatus.CONFLICT);
    }
}
