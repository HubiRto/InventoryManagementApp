package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.AddProductDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateProductDTO;
import pl.pomoku.inventorymanagementapp.entity.*;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProducentRepository producentRepository;
    private final AttributeRepository attributeRepository;
    private final ProductAttributeRepository productAttributeRepository;

    @Transactional
    public Product addNewProduct(AddProductDTO request) {
        if(productRepository.existsByName(request.getName())) {
            throw new AppException("Product with this name already exist", HttpStatus.CONFLICT);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new AppException("Store not found", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Producent producent = producentRepository.findById(request.getProducentId())
                .orElseThrow(() -> new AppException("Producent not found", HttpStatus.NOT_FOUND));


        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setStore(store);
        product.setCreatedBy(user);
        product.setProducent(producent);
        product.setCreatedAt(LocalDateTime.now());

        product = productRepository.saveAndFlush(product);

        Stock stock = new Stock();
        stock.setQuantity(request.getQuantity());
        stock.setProduct(product);

        product.setStock(stock);

        Price price = new Price();
        price.setNetPrice(request.getNetPrice());
        price.setVat(request.getVat());
        price.setProduct(product);

        product.setPrice(price);

        Product finalProduct = product;
        List<ProductAttribute> productAttributes = request.getAttributes().entrySet().stream()
                .map(entry -> {
                    Attribute attribute = attributeRepository.findByName(entry.getKey())
                            .orElseGet(() -> {
                                Attribute newAttribute = new Attribute();
                                newAttribute.setName(entry.getKey());
                                return attributeRepository.save(newAttribute);
                            });

                    return new ProductAttribute(finalProduct, attribute, entry.getValue());
                }).toList();

        product.getProductAttributes().addAll(productAttributes);
        productAttributeRepository.saveAll(productAttributes);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(UpdateProductDTO request, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        Producent producent = producentRepository.findById(request.getProducentId())
                .orElseThrow(() -> new AppException("Producent not found", HttpStatus.NOT_FOUND));

        Stock stock = product.getStock();
        stock.setQuantity(request.getQuantity());

        Price price = product.getPrice();
        price.setNetPrice(request.getNetPrice());
        price.setVat(request.getVat());

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setProducent(producent);


        Map<String, String> attributesMap = request.getAttributes();
        List<ProductAttribute> existingProductAttributes = product.getProductAttributes();

        for (Map.Entry<String, String> entry : attributesMap.entrySet()) {
            String attributeName = entry.getKey();
            String attributeValue = entry.getValue();

            Attribute attribute = attributeRepository.findByName(attributeName)
                    .orElseGet(() -> {
                        Attribute newAttribute = new Attribute();
                        newAttribute.setName(attributeName);
                        return attributeRepository.save(newAttribute);
                    });

            Optional<ProductAttribute> optionalProductAttribute = existingProductAttributes.stream()
                    .filter(pa -> pa.getAttribute().equals(attribute))
                    .findFirst();

            if (optionalProductAttribute.isPresent()) {
                //Update existing ProductAttribute
                ProductAttribute productAttribute = optionalProductAttribute.get();
                productAttribute.setValue(attributeValue);
            } else {
                //Create new ProductAttribute
                ProductAttribute productAttribute = new ProductAttribute(product, attribute, attributeValue);
                product.getProductAttributes().add(productAttribute);
            }
        }

        // Remove attributes that are in the database, but are no longer in the update request
        List<ProductAttribute> attributesToRemove = existingProductAttributes.stream()
                .filter(pa -> !attributesMap.containsKey(pa.getAttribute().getName()))
                .collect(Collectors.toList());

        product.getProductAttributes().removeAll(attributesToRemove);
        productAttributeRepository.deleteAll(attributesToRemove);

        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }


    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
    }

    public List<Product> getAllProductsByStoreId(UUID storeId) {
        return productRepository.findAllByStoreId(storeId);
    }

    @Transactional
    public void deleteByProductById(Long id) {
        Product product = getProductById(id);
        //Optionally delete all attributes where only have reference to one product
        productRepository.delete(product);
    }
}
