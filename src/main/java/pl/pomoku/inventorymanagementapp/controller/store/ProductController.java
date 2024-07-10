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
import pl.pomoku.inventorymanagementapp.dto.request.AddProductDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateProductDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProductDetailsDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProductSummaryDTO;
import pl.pomoku.inventorymanagementapp.mapper.ProductMapper;
import pl.pomoku.inventorymanagementapp.service.ProductService;
import pl.pomoku.inventorymanagementapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ProductSummaryDTO>> getAllProductsByStoreId(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId
    ) {
        return new ResponseEntity<>(
                productService.getAllProductsByStoreId(storeId).stream()
                        .map(productMapper::mapToSummaryDTO)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsDTO> getProductById(
            @NotNull(message = "Product ID cannot be null")
            @Min(value = 1, message = "Product ID must be a non-negative number")
            @PathVariable("productId") Long productId
    ) {
        return new ResponseEntity<>(
                productMapper.mapToProductDetailsDTO(productService.getProductById(productId)),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<ProductSummaryDTO> createProduct(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AddProductDTO request
    ) {
        return new ResponseEntity<>(
                productMapper.mapToSummaryDTO(productService.addNewProduct(
                        request,
                        userService.getUserFromToken(token),
                        storeId
                )),
                HttpStatus.CREATED
        );
    }

    @Secured("ADMIN")
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductSummaryDTO> createProduct(
            @NotNull(message = "Product ID cannot be null")
            @Min(value = 1, message = "Product ID must be a non-negative number")
            @PathVariable("productId") Long productId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateProductDTO request
    ) {
        return new ResponseEntity<>(
                productMapper.mapToSummaryDTO(productService.updateProduct(
                        request,
                        userService.getUserFromToken(token),
                        productId
                )),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @NotNull(message = "Product ID cannot be null")
            @Min(value = 1, message = "Product ID must be a non-negative number")
            @PathVariable("productId") Long productId
    ) {
        productService.deleteByProductById(productId, userService.getUserFromToken(token));
        return ResponseEntity.ok("Successfully deleted product");
    }
}