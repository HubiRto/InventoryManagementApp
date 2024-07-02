package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.AddCategoryRequest;
import pl.pomoku.inventorymanagementapp.exception.CategoryAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.CategoryNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.CategoryRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Set<String> getAllNames() {
        return categoryRepository.findDistinctNames();
    }

    public void saveCategory(AddCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new CategoryAlreadyExistException();
        }
        categoryRepository.save(request.mapToEntity());
    }

    @Transactional
    public void deleteCategoryByName(String name) {
        if (!categoryRepository.existsByName(name)) {
            throw new CategoryNotFoundException(name);
        }
        categoryRepository.deleteByName(name);
    }
}
