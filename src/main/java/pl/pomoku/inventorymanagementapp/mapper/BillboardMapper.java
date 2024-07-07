package pl.pomoku.inventorymanagementapp.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardDTO;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardNameDTO;
import pl.pomoku.inventorymanagementapp.dto.response.CategoryDTO;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.entity.Category;

@Component
@RequiredArgsConstructor
public class BillboardMapper {
    private final ModelMapper modelMapper;

    public BillboardDTO mapToBillboardDTO(Billboard billboard) {
        return modelMapper.map(billboard, BillboardDTO.class);
    }

    public BillboardNameDTO mapToBillboardNameDTO(Billboard billboard) {
        return modelMapper.map(billboard, BillboardNameDTO.class);
    }
}
