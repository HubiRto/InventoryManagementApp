package pl.pomoku.inventorymanagementapp.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardDTO;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardNameDTO;
import pl.pomoku.inventorymanagementapp.dto.response.CategoryDTO;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.entity.Category;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public interface BillboardMapper {
    BillboardDTO mapToBillboardDTO(Billboard entity);
    BillboardNameDTO mapToBillboardNameDTO(Billboard entity);
}
