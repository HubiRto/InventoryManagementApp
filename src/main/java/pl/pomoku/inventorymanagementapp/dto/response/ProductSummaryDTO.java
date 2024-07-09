package pl.pomoku.inventorymanagementapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummaryDTO {
    private String name;
    private Long producentId;
    private String producentName;
    private boolean availableInStock;
    private double netPrice;
    private double grossPrice;
}