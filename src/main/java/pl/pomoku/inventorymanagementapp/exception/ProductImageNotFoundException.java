
package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class ProductImageNotFoundException extends AppException {
    public ProductImageNotFoundException() {
        super("Product image not found", HttpStatus.NOT_FOUND);
    }
}
