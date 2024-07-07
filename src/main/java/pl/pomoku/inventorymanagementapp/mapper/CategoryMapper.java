package pl.pomoku.inventorymanagementapp.mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.pomoku.inventorymanagementapp.dto.request.CategoryRequest;
import pl.pomoku.inventorymanagementapp.dto.response.CategoryDTO;
import pl.pomoku.inventorymanagementapp.entity.Category;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    @Transactional
    public CategoryDTO mapToCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getBillboard() != null ? category.getBillboard().getId() : null,
                category.getBillboard() != null ? category.getBillboard().getLabel() : null
        );
    }

    public Category mapToCategory(CategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
