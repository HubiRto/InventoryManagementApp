package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pomoku.inventorymanagementapp.dto.request.AddProductRequest;
import pl.pomoku.inventorymanagementapp.dto.response.ProductResponse;
import pl.pomoku.inventorymanagementapp.entity.Product;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.service.ProductService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts().stream().map(Product::mapToDto).toList();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId).mapToDto());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody AddProductRequest request) {
        return new ResponseEntity<>(productService.saveProduct(request).mapToDto(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Long productId, @RequestParam("image") MultipartFile imageFile) {
        try {
            productService.saveProductImage(productId, imageFile);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            throw new AppException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        try {
            byte[] imageData = productService.getProductImage(productId);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
        } catch (IOException e) {
            throw new AppException("Failed to retrieve image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
