package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.CreateCategoryDTO;
import pl.pomoku.inventorymanagementapp.entity.*;
import pl.pomoku.inventorymanagementapp.enumerated.EventType;
import pl.pomoku.inventorymanagementapp.exception.billboard.BillboardNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.category.CategoryAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.category.CategoryCyclicDependencyException;
import pl.pomoku.inventorymanagementapp.exception.category.CategoryNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.store.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.mapper.CategoryMapper;
import pl.pomoku.inventorymanagementapp.repository.BillboardRepository;
import pl.pomoku.inventorymanagementapp.repository.CategoryRepository;
import pl.pomoku.inventorymanagementapp.repository.EventRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final BillboardRepository billboardRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;


    public List<Category> getAllCategoriesByStoreIdByTree(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
        return categoryRepository.findAllWithoutParentByStoreId(store.getId());
    }

    public List<Category> getAllCategoriesByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
        return categoryRepository.findAllByStoreId(store.getId());
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Transactional
    public Category addNewCategory(CreateCategoryDTO request, User user, Long storeId) {
        if (categoryRepository.existsByNameAndStoreId(request.getName(), storeId)) {
            throw new CategoryAlreadyExistException();
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        Category category = categoryMapper.mapToEntity(request);
        category.setCreatedAt(LocalDateTime.now());
        category.setStore(store);
        category.setCreatedBy(user);

        Event event = new Event(
                EventType.CREATE,
                "Admin (%s) create category with name (%s)"
                        .formatted(user.getFullName(), category.getName())
        );

        eventRepository.save(event);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(CreateCategoryDTO request, User user, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        List<Event> events = new ArrayList<>();

        if (!category.getName().equals(request.getName())) {
            events.add(new Event(
                    EventType.UPDATE,
                    "Admin (%s) change category name from (%s) to (%s)"
                            .formatted(user.getFullName(), category.getName(), request.getName())
            ));
            category.setName(request.getName());
        }

        if (!category.getBillboard().getId().equals(request.getBillboardId())) {
            Billboard newBillboard = billboardRepository.findById(request.getBillboardId())
                    .orElseThrow(() -> new BillboardNotFoundException(request.getBillboardId()));

            events.add(new Event(
                    EventType.UPDATE,
                    "Admin (%s) change billboard in category from (%s) to (%s)"
                            .formatted(user.getFullName(), category.getBillboard().getId(), request.getBillboardId())
            ));
            category.setBillboard(newBillboard);
        }


        Long currentParentId = category.getParent() != null ? category.getParent().getId() : null;
        Long newParentId = request.getCategoryParentId();

        if (!Objects.equals(currentParentId, newParentId)) {
            Category newParent = null;

            if (newParentId != null) {
                newParent = categoryRepository.findById(newParentId)
                        .orElseThrow(() -> new CategoryNotFoundException(newParentId));

                if (isCyclicDependency(category, newParent)) {
                    throw new CategoryCyclicDependencyException();
                }
            }

            events.add(new Event(
                    EventType.UPDATE,
                    "Admin (%s) change parent of category (%s) from (%s) to (%s)"
                            .formatted(
                                    user.getFullName(),
                                    category.getName(),
                                    currentParentId != null ? currentParentId.toString() : "none",
                                    newParentId != null ? newParentId.toString() : "none"
                            )
            ));

            category.setParent(newParent);
        }

        if (!events.isEmpty()) {
            eventRepository.saveAll(events);
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Event event = new Event(
                EventType.DELETE,
                "Admin (%s) delete category (%s)".formatted(user.getFullName(), category.getName())
        );
        eventRepository.save(event);

        categoryRepository.delete(category);
    }

    private boolean isCyclicDependency(Category category, Category newParent) {
        Category current = newParent;
        while (current != null) {
            if (current.equals(category)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
