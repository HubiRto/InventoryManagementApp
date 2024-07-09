package pl.pomoku.inventorymanagementapp.exception.attribute;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class AttributeAlreadyExistException extends AppException {
    public AttributeAlreadyExistException() {
        super("Attribute with this name already exist", HttpStatus.CONFLICT);
    }
}
