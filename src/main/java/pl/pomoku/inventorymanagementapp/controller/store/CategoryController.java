package pl.pomoku.inventorymanagementapp.controller.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.CreateCategoryDTO;
import pl.pomoku.inventorymanagementapp.dto.response.CategoryDTO;
import pl.pomoku.inventorymanagementapp.mapper.CategoryMapper;
import pl.pomoku.inventorymanagementapp.service.CategoryService;
import pl.pomoku.inventorymanagementapp.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategoriesByStoreId(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId
    ) {
        return ResponseEntity.ok(
                categoryService.getAllCategoriesByStoreId(storeId).stream()
                        .map(categoryMapper::mapToDTO)
                        .toList()
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @NotNull(message = "Category ID cannot be null")
            @Min(value = 1, message = "Category ID must be a non-negative number")
            @PathVariable("categoryId") Long categoryId
    ) {
        return ResponseEntity.ok(
                categoryMapper.mapToDTO(categoryService.getCategoryById(categoryId))
        );
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<CategoryDTO> addNewCategory(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateCategoryDTO request
    ) {
        return new ResponseEntity<>(
                categoryMapper.mapToDTO(categoryService.addNewCategory(
                        request,
                        userService.getUserFromToken(token),
                        storeId
                )),
                HttpStatus.CREATED
        );
    }

    @Secured("ADMIN")
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategoryBy(
            @NotNull(message = "Category ID cannot be null")
            @Min(value = 1, message = "Category ID must be a non-negative number")
            @PathVariable("categoryId") Long categoryId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateCategoryDTO request
    ) {
        return new ResponseEntity<>(
                categoryMapper.mapToDTO(categoryService.updateCategory(
                        request,
                        userService.getUserFromToken(token),
                        categoryId
                )),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(
            @NotNull(message = "Category ID cannot be null")
            @Min(value = 1, message = "Category ID must be a non-negative number")
            @PathVariable Long categoryId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token
    ) {
        categoryService.deleteCategory(categoryId, userService.getUserFromToken(token));
        return ResponseEntity.ok("Successfully deleted category");
    }
}
