package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.AddProductDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateProductDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProductDetailsDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProductSummaryDTO;
import pl.pomoku.inventorymanagementapp.mapper.ProductMapper;
import pl.pomoku.inventorymanagementapp.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductSummaryDTO>> getAllProductsByStoreId(@RequestParam("storeId") UUID storeId) {
        return new ResponseEntity<>(
                productService.getAllProductsByStoreId(storeId).stream()
                        .map(productMapper::mapToSummaryDTO)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsDTO> getProductById(@PathVariable("productId") Long productId) {
        return new ResponseEntity<>(
                productMapper.mapToProductDetailsDTO(productService.getProductById(productId)),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ProductSummaryDTO> createProduct(
            @RequestBody AddProductDTO request
    ) {
        return new ResponseEntity<>(
                productMapper.mapToSummaryDTO(productService.addNewProduct(request)),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductSummaryDTO> createProduct(
            @RequestBody UpdateProductDTO request,
            @PathVariable("productId") Long productId
    ) {
        return new ResponseEntity<>(
                productMapper.mapToSummaryDTO(productService.updateProduct(request, productId)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteByProductById(productId);
        return ResponseEntity.ok("Successfully deleted product");
    }
}