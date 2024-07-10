package pl.pomoku.inventorymanagementapp.exception.billboard;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class BillboardAlreadyExistException extends AppException {
    public BillboardAlreadyExistException(String label) {
        super("Billboard with this label %s already exist".formatted(label), HttpStatus.CONFLICT);
    }
}
