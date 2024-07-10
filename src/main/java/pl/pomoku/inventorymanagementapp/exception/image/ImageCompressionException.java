package pl.pomoku.inventorymanagementapp.exception.image;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ImageCompressionException extends AppException {
    public ImageCompressionException() {
        super("Failed to compress image", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
