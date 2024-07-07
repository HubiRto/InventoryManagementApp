package pl.pomoku.inventorymanagementapp.exception;

import org.springframework.http.HttpStatus;

public class StoreNameRequiredException extends AppException {
    public StoreNameRequiredException() {
        super("Store name is empty or blank", HttpStatus.BAD_REQUEST);
    }
}
