package pl.pomoku.inventorymanagementapp.mapper;

import org.mapstruct.Mapper;
import pl.pomoku.inventorymanagementapp.dto.request.CreateProducentDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProducentDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProducentNameDTO;
import pl.pomoku.inventorymanagementapp.entity.Producent;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public interface ProducentMapper {
    ProducentDTO mapToDTO(Producent entity);
    Producent mapToEntity(CreateProducentDTO dto);
    ProducentNameDTO mapToNameDTO(Producent entity);
}