package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.CategoryRequest;
import pl.pomoku.inventorymanagementapp.dto.response.CategoryDTO;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.mapper.CategoryMapper;
import pl.pomoku.inventorymanagementapp.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategoriesByStoreId(@RequestParam("storeId") UUID storeId) {
        if (storeId == null) throw new AppException("Store Id is required", HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(
                categoryService.getAllCategoriesByStoreId(storeId).stream()
                        .map(categoryMapper::mapToCategoryDTO)
                        .toList()
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("categoryId") UUID categoryId) {
        return ResponseEntity.ok(
                categoryMapper.mapToCategoryDTO(categoryService.getCategoryById(categoryId))
        );
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> addNewCategory(
            @RequestBody CategoryRequest request,
            @RequestParam("storeId") UUID storeId
    ) {
        log.info("request: {}", request);
        return ResponseEntity.ok(
                categoryMapper.mapToCategoryDTO(categoryService.addNewCategory(request, storeId))
        );
    }

    @PatchMapping
    public ResponseEntity<CategoryDTO> getCategoriesNamesByStoreId(
            @RequestBody CategoryRequest request,
            @RequestParam("categoryId") UUID categoryId
    ) {
        return ResponseEntity.ok(
                categoryMapper.mapToCategoryDTO(categoryService.updateCategory(request, categoryId))
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Successfully deleted category");
    }
}
