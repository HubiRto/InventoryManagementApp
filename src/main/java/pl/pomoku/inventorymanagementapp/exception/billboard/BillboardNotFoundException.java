package pl.pomoku.inventorymanagementapp.exception.billboard;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class BillboardNotFoundException extends AppException {
    public BillboardNotFoundException(Long id) {
        super(String.format("Billboard with id: %s not found", id), HttpStatus.NOT_FOUND);
    }
}
