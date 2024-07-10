
package pl.pomoku.inventorymanagementapp.exception.product;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ProductImageNotFoundException extends AppException {
    public ProductImageNotFoundException() {
        super("Product image not found", HttpStatus.NOT_FOUND);
    }
}
