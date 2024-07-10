package pl.pomoku.inventorymanagementapp.mapper;

import org.mapstruct.Mapper;
import pl.pomoku.inventorymanagementapp.dto.request.CreateStoreDTO;
import pl.pomoku.inventorymanagementapp.dto.response.StoreDTO;
import pl.pomoku.inventorymanagementapp.entity.Store;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public interface StoreMapper {
    Store mapToEntity(CreateStoreDTO dto);
    StoreDTO mapToDto(Store store);
}
