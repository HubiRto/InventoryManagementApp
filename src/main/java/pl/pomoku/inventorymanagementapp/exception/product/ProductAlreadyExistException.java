package pl.pomoku.inventorymanagementapp.exception.product;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ProductAlreadyExistException extends AppException {
    public ProductAlreadyExistException() {
        super("Product with this name already exist", HttpStatus.CONFLICT);
    }
}
