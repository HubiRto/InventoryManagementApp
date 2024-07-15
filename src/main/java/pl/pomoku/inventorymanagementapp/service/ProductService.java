package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pomoku.inventorymanagementapp.dto.request.AddProductDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateProductDTO;
import pl.pomoku.inventorymanagementapp.entity.*;
import pl.pomoku.inventorymanagementapp.enumerated.EventType;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.exception.producent.ProducentNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.productImage.ProductImageNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.*;
import pl.pomoku.inventorymanagementapp.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ProducentRepository producentRepository;
    private final AttributeRepository attributeRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final EventRepository eventRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public Product addNewProduct(AddProductDTO request, User user, Long storeId) {
        if (productRepository.existsByName(request.getName())) {
            throw new AppException("Product with this name already exist", HttpStatus.CONFLICT);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new AppException("Store not found", HttpStatus.NOT_FOUND));

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

                                Event event = new Event(
                                        EventType.CREATE,
                                        "Admin (%s) create attribute with name (%s)"
                                                .formatted(user.getFullName(), newAttribute.getName())
                                );
                                eventRepository.save(event);

                                return attributeRepository.save(newAttribute);
                            });

                    return new ProductAttribute(finalProduct, attribute, entry.getValue());
                }).toList();

        product.getProductAttributes().addAll(productAttributes);
        productAttributeRepository.saveAll(productAttributes);


        Event event = new Event(
                EventType.CREATE,
                "Admin (%s) create product with name (%s)"
                        .formatted(user.getFullName(), product.getName())
        );
        eventRepository.save(event);

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(UpdateProductDTO request, User user, Long productId) {
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

                        Event event = new Event(
                                EventType.CREATE,
                                "Admin (%s) create attribute with name (%s)"
                                        .formatted(user.getFullName(), newAttribute.getName())
                        );
                        eventRepository.save(event);

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

    public List<Product> getAllProductsByStoreId(Long storeId) {
        return productRepository.findAllByStoreId(storeId);
    }

    @Transactional
    public List<String> getAllProductImagesById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProducentNotFoundException(productId))
                .getImages().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductImage getAllProductImageByImageId(Long productImageId) {
        return productImageRepository.findById(productImageId)
                .orElseThrow(() -> new ProductImageNotFoundException(productImageId));
    }

    @Transactional
    public Product addNewProductImagesById(Long productId, User user, List<MultipartFile> files) {
        Product product = getProductById(productId);

        for (MultipartFile file : files) {
            if (file == null || file.getSize() == 0 || file.getContentType() == null) {
                throw new AppException("File or Content Type is empty", HttpStatus.BAD_REQUEST);
            }

            try {
                ProductImage image = ProductImage.builder()
                        .imageUrl("http://localhost:8080/api/v1/products/%d/images".formatted(productId))
                        .image(ImageUtils.compressImage(file.getBytes()))
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .product(product)
                        .build();
                image = productImageRepository.save(image);
                image.setImageUrl("http://localhost:8080/api/v1/products/images/%d".formatted(image.getId()));
                image = productImageRepository.save(image);

                Event event = new Event(
                        EventType.CREATE,
                        "Admin (%s) create new product image (%s)".formatted(user.getFullName(), image.getFileName())
                );
                eventRepository.save(event);
            } catch (IOException exception) {
                throw new AppException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProductImagesById(Long productId, User user, List<MultipartFile> files) {
        Product product = getProductById(productId);

        List<byte[]> compressedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.getSize() == 0 || file.getContentType() == null) {
                throw new AppException("File or Content Type is empty", HttpStatus.BAD_REQUEST);
            }

            try {
                byte[] compressedImage = ImageUtils.compressImage(file.getBytes());
                compressedImages.add(compressedImage);

                Optional<ProductImage> existingImageOpt = product.getImages().stream()
                        .filter(img -> Arrays.equals(img.getImage(), compressedImage))
                        .findFirst();

                ProductImage image;
                if (existingImageOpt.isPresent()) {
                    image = existingImageOpt.get();
                    image.setFileType(file.getContentType());
                } else {
                    image = ProductImage.builder()
                            .imageUrl("http://localhost:8080/api/v1/products/%d/images".formatted(productId))
                            .image(compressedImage)
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .product(product)
                            .build();
                }
                productImageRepository.save(image);

                Event event = new Event(
                        existingImageOpt.isPresent() ? EventType.UPDATE : EventType.CREATE,
                        "Admin (%s) %s product image (%s)".formatted(
                                user.getFullName(),
                                existingImageOpt.isPresent() ? "updated" : "created",
                                image.getFileName()
                        )
                );
                eventRepository.save(event);
            } catch (IOException exception) {
                throw new AppException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Remove images not present in the uploaded files
        List<ProductImage> imagesToRemove = product.getImages().stream()
                .filter(img -> !compressedImages.contains(img.getImage()))
                .toList();

        for (ProductImage imageToRemove : imagesToRemove) {
            productImageRepository.delete(imageToRemove);

            Event event = new Event(
                    EventType.DELETE,
                    "Admin (%s) deleted product image (%s)".formatted(user.getFullName(), imageToRemove.getFileName())
            );
            eventRepository.save(event);
        }

        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteByProductById(Long id, User user) {
        Product product = getProductById(id);

        Event event = new Event(
                EventType.DELETE,
                "Admin (%s) delete product (%s)".formatted(user.getFullName(), product.getName())
        );
        eventRepository.save(event);


        //Optionally delete all attributes where only have reference to one product
        productRepository.delete(product);
    }
}
