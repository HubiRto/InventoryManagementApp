package pl.pomoku.inventorymanagementapp.exception.attribute;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class AttributeNotFoundException extends AppException {
    public AttributeNotFoundException() {
        super("Attribute with this id does not exist", HttpStatus.NOT_FOUND);
    }
}
