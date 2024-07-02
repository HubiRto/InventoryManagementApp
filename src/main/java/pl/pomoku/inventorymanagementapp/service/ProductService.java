package pl.pomoku.inventorymanagementapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.AddProductRequest;
import pl.pomoku.inventorymanagementapp.entity.Category;
import pl.pomoku.inventorymanagementapp.entity.Product;
import pl.pomoku.inventorymanagementapp.exception.CategoryNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.ProductAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.ProductNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.CategoryRepository;
import pl.pomoku.inventorymanagementapp.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product saveProduct(AddProductRequest request) {
        if (productRepository.existsByName(request.name())) {
            throw new ProductAlreadyExistException();
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        return productRepository.save(request.mapToEntity(category));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
