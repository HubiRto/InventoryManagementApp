package pl.pomoku.inventorymanagementapp.dto.request;

import pl.pomoku.inventorymanagementapp.entity.Category;

public record AddCategoryRequest(
        String name,
        String description
) {
    public Category mapToEntity() {
        return Category.builder()
                .name(name)
                .description(description)
                .build();
    }
}
