package pl.pomoku.inventorymanagementapp.exception.productImage;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ProductImageNotFoundException extends AppException {
    public ProductImageNotFoundException(Long productImageId) {
        super("Product Image with id %d does not exist".formatted(productImageId), HttpStatus.NOT_FOUND);
    }
}
