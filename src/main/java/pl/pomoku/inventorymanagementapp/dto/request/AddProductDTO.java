package pl.pomoku.inventorymanagementapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductDTO {
    @NotNull(message = "Product name cannot be null")
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 3, message = "Product name must have at least 3 characters")
    private String name;

    @NotNull(message = "Product description cannot be null")
    @NotEmpty(message = "Product description cannot be empty")
    @Size(min = 3, message = "Product description must have at least 3 characters")
    private String description;

    @NotNull(message = "Category ID cannot be null")
    @Min(value = 1, message = "Category ID must be a non-negative number")
    private Long categoryId;

    @NotNull(message = "Producent ID cannot be null")
    @Min(value = 1, message = "Producent ID must be a non-negative number")
    private Long producentId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be a non-negative number")
    private int quantity;

    @NotNull(message = "Netto price cannot be null")
    @Min(value = 1, message = "Netto price must be a non-negative number")
    private double netPrice;

    @NotNull(message = "Vat cannot be null")
    @Min(value = 0, message = "Vat must be a non-negative number")
    private double vat;

    @NotNull(message = "Attributes cannot be null")
    private Map<String, String> attributes;
}
