package pl.pomoku.inventorymanagementapp.exception.image;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ImageDecompressionException extends AppException {
    public ImageDecompressionException() {
        super("Failed to decompress image", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
