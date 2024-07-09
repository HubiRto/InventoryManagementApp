package pl.pomoku.inventorymanagementapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDTO {
    private String name;
    private String description;
    private UUID categoryId;
    private Long producentId;
    private int quantity;
    private double netPrice;
    private double vat;
    private Map<String, String> attributes;
}
