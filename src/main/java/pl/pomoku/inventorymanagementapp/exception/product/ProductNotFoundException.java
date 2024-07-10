package pl.pomoku.inventorymanagementapp.exception.product;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ProductNotFoundException extends AppException {
    public ProductNotFoundException(Long id) {
        super(String.format("Product with id: %s not found", id), HttpStatus.NOT_FOUND);
    }

    public ProductNotFoundException() {
        super("Product not found", HttpStatus.NOT_FOUND);
    }
}
