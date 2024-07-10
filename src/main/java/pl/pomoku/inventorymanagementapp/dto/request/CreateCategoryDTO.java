package pl.pomoku.inventorymanagementapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateCategoryDTO {
    @NotNull(message = "Category name cannot be null")
    @NotEmpty(message = "Category name cannot be empty")
    @Size(min = 3, message = "Category name must have at least 3 characters")
    private String name;

    @NotNull(message = "Billboard ID cannot be null")
    @Min(value = 1, message = "Billboard ID must be a non-negative number")
    private Long billboardId;
    private Long categoryParentId;
}
