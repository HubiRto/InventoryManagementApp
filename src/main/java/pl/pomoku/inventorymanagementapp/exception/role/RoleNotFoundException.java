package pl.pomoku.inventorymanagementapp.exception.role;

import org.springframework.http.HttpStatus;
import pl.pomoku.inventorymanagementapp.exception.AppException;

public class RoleNotFoundException extends AppException {
    public RoleNotFoundException(String name) {
        super("Role with name %s does not exist".formatted(name), HttpStatus.NOT_FOUND);
    }
}
