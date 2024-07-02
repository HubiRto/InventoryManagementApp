package pl.pomoku.inventorymanagementapp.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        int quantity,
        BigDecimal price,
        String category
) {
}
