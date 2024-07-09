package pl.pomoku.inventorymanagementapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.pomoku.inventorymanagementapp.dto.request.CreateAttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.response.AttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProductDetailsDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProductSummaryDTO;
import pl.pomoku.inventorymanagementapp.entity.Attribute;
import pl.pomoku.inventorymanagementapp.entity.Product;
import pl.pomoku.inventorymanagementapp.entity.ProductAttribute;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public interface AttributeMapper {
    AttributeMapper INSTANCE = Mappers.getMapper(AttributeMapper.class);

    AttributeDTO mapToDTO(Attribute entity);
    Attribute mapToEntity(CreateAttributeDTO dto);
}
