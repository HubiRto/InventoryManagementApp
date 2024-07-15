package pl.pomoku.inventorymanagementapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private Long producentId;
    private String producentName;
    private Long categoryId;
    private int quantity;
    private double netPrice;
    private double grossPrice;
    private double vat;
    private List<String> images;
    private Map<String, String> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
