package pl.pomoku.inventorymanagementapp.dto.request;

import pl.pomoku.inventorymanagementapp.entity.Category;
import pl.pomoku.inventorymanagementapp.entity.Product;

import java.math.BigDecimal;

public record AddProductRequest(
        String name,
        String description,
        Long categoryId,
        int quantity,
        BigDecimal price
) {
    public Product mapToEntity(Category category) {
        return Product.builder()
                .name(name)
                .category(category)
                .description(description)
                .quantity(quantity)
                .price(price)
                .build();
    }
}
