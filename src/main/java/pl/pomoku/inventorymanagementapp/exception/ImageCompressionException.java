package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class ImageCompressionException extends AppException {
    public ImageCompressionException() {
        super("Failed to compress image", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
