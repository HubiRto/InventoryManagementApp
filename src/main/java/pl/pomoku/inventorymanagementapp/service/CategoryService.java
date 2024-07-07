package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.CategoryRequest;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.entity.Category;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.exception.*;
import pl.pomoku.inventorymanagementapp.mapper.CategoryMapper;
import pl.pomoku.inventorymanagementapp.repository.BillboardRepository;
import pl.pomoku.inventorymanagementapp.repository.CategoryRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final BillboardRepository billboardRepository;
    private final CategoryMapper categoryMapper;


    public List<Category> getAllCategoriesByStoreId(UUID storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException(storeId));
        return categoryRepository.findAllByStore(store);
    }

    public Category getCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    public Category addNewCategory(CategoryRequest request, UUID storeId) {
        if(categoryRepository.existsByNameAndStoreId(request.getName(), storeId)) {
            throw new CategoryAlreadyExistException();
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        Billboard billboard = billboardRepository.findById(request.getBillboardId())
                .orElseThrow(() -> new BillboardNotFoundException(request.getBillboardId()));

        Category category = categoryMapper.mapToCategory(request);
        category.setStore(store);
        category.setBillboard(billboard);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(CategoryRequest request, UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);


        if(!category.getName().equals(request.getName())) {
            category.setName(request.getName());
        }

        if (category.getBillboard() == null || !category.getBillboard().getId().equals(request.getBillboardId())) {
            Billboard billboard = billboardRepository.findById(request.getBillboardId())
                    .orElseThrow(() -> new BillboardNotFoundException(request.getBillboardId()));
            category.setBillboard(billboard);
        }

        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }
}
