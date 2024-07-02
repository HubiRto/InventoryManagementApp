package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class ImageDecompressionException extends AppException {
    public ImageDecompressionException() {
        super("Failed to decompress image", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
