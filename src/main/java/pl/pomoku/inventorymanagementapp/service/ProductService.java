package pl.pomoku.inventorymanagementapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pomoku.inventorymanagementapp.dto.request.AddProductRequest;
import pl.pomoku.inventorymanagementapp.entity.Category;
import pl.pomoku.inventorymanagementapp.entity.Product;
import pl.pomoku.inventorymanagementapp.entity.ProductImage;
import pl.pomoku.inventorymanagementapp.exception.CategoryNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.ProductAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.ProductImageNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.ProductNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.CategoryRepository;
import pl.pomoku.inventorymanagementapp.repository.ProductImageRepository;
import pl.pomoku.inventorymanagementapp.repository.ProductRepository;
import pl.pomoku.inventorymanagementapp.utils.ImageUtils;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

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

    public void saveProductImage(Long id, MultipartFile file) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        ProductImage productImage = productImageRepository.findByProductId(id).orElseGet(ProductImage::new);

        productImage.setName(file.getOriginalFilename());
        productImage.setType(file.getContentType());
        productImage.setImage(ImageUtils.compressImage(file.getBytes()));
        productImage.setProduct(product);
        productImage = productImageRepository.save(productImage);

        product.setImage(productImage);
        productRepository.save(product);
    }

    public byte[] getProductImage(Long id) throws IOException {
        if (!productImageRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        return ImageUtils.decompressImage(productImageRepository.findById(id)
                .orElseThrow(ProductImageNotFoundException::new).getImage());
    }
}
