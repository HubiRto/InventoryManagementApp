package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.AddCategoryRequest;
import pl.pomoku.inventorymanagementapp.service.CategoryService;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Set<String> getAllCategoryNames() {
        return categoryService.getAllNames();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@RequestBody AddCategoryRequest request) {
        categoryService.saveCategory(request);
        return new ResponseEntity<>("Category successfully created", HttpStatus.CREATED);
    }

    @DeleteMapping("/{categoryName}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryName) {
        categoryService.deleteCategoryByName(categoryName);
        return new ResponseEntity<>(String.format("Category with name: %s successfully deleted", categoryName), HttpStatus.OK);
    }
}
