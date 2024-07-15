package pl.pomoku.inventorymanagementapp.exception.producent;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ProducentAlreadyExistException extends AppException {
    public ProducentAlreadyExistException(String name) {
        super("Producent with name %s already exist".formatted(name), HttpStatus.CONFLICT);
    }
}
