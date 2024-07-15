package pl.pomoku.inventorymanagementapp.exception.producent;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class ProducentNotFoundException extends AppException {
    public ProducentNotFoundException(Long producentId) {
        super("Producent with id %d does not exist".formatted(producentId), HttpStatus.NOT_FOUND);
    }
}
