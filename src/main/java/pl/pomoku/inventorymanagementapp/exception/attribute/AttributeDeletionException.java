package pl.pomoku.inventorymanagementapp.exception.attribute;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class AttributeDeletionException extends AppException {
    public AttributeDeletionException() {
        super("Cannot delete attribute because it is associated with existing products.", HttpStatus.CONFLICT);
    }
}
