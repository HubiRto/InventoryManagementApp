package pl.pomoku.inventorymanagementapp.mapper;

import org.mapstruct.Mapper;
import pl.pomoku.inventorymanagementapp.dto.response.UserDTO;
import pl.pomoku.inventorymanagementapp.entity.User;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public interface UserMapper {
    UserDTO userToUserDTO(User user);
}
