package pl.pomoku.inventorymanagementapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAttributeDTO {
    @NotNull(message = "Attribute name cannot be null")
    @NotEmpty(message = "Attribute name cannot be empty")
    @Size(min = 3, message = "Attribute name must have at least 3 characters")
    private String name;
}
