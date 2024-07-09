package pl.pomoku.inventorymanagementapp.exception.attribute;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class AttributeNameNotChangeException extends AppException {
    public AttributeNameNotChangeException() {
        super("Attribute name don't change", HttpStatus.BAD_REQUEST);
    }
}
