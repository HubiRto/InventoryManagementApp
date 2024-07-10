package pl.pomoku.inventorymanagementapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import pl.pomoku.inventorymanagementapp.dto.request.CreateCategoryDTO;
import pl.pomoku.inventorymanagementapp.dto.response.CategoryDTO;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.entity.Category;
import pl.pomoku.inventorymanagementapp.exception.billboard.BillboardNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.category.CategoryNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.BillboardRepository;
import pl.pomoku.inventorymanagementapp.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public abstract class CategoryMapper {
    @Autowired
    protected BillboardRepository billboardRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Mappings({
            @Mapping(source = "billboard.id", target = "billboardId"),
            @Mapping(source = "billboard.label", target = "billboardLabel"),
            @Mapping(source = "children", target = "children", qualifiedByName = "mapChildren")
    })
    public abstract CategoryDTO mapToDTO(Category entity);

    @Mappings({
            @Mapping(source = "billboardId", target = "billboard", qualifiedByName = "mapBillboardIdToBillboard"),
            @Mapping(source = "categoryParentId", target = "parent", qualifiedByName = "mapCategoryParentIdToCategory")
    })
    public abstract Category mapToEntity(CreateCategoryDTO dto);

    @Named("mapBillboardIdToBillboard")
    protected Billboard mapBillboardIdToBillboard(Long billboardId) {
        return billboardRepository.findById(billboardId)
                .orElseThrow(() -> new BillboardNotFoundException(billboardId));
    }

    @Named("mapCategoryParentIdToCategory")
    protected Category mapCategoryParentIdToCategory(Long categoryParentId) {
        return categoryParentId != null ? categoryRepository.findById(categoryParentId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryParentId)) : null;
    }

    @Named("mapChildren")
    protected List<CategoryDTO> mapChildren(List<Category> children) {
        return children.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
