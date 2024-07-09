package pl.pomoku.inventorymanagementapp.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pomoku.inventorymanagementapp.dto.response.UserDTO;
import pl.pomoku.inventorymanagementapp.mapper.UserMapper;
import pl.pomoku.inventorymanagementapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Retrieves the list of all users.
     *
     * @return A list of all users as UserDTO objects
     *
     * @apiNote This endpoint is secured with the ADMIN role.
     *
     * @response 200 Returns the list of users.
     */

    @Secured("ADMIN")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUser().stream()
                .map(userMapper::userToUserDTO)
                .toList();
    }

    /**
     * Retrieves the details of a specific user by their ID.
     *
     * @param userId The ID of the user to be retrieved. It must not be null and must be a non-negative number.
     * @return The details of the user as a UserDTO object
     *
     * @apiNote This endpoint is secured with the ADMIN role.
     *
     * @response 200 Returns the details of the user.
     * @response 404 User not found for the given ID.
     * @response 400 Bad request, e.g., if the user ID is invalid.
     */

    @Secured("ADMIN")
    @GetMapping("{userId}")
    public UserDTO getUserById(
            @PathVariable
            @NotNull(message = "User ID cannot be null")
            @Min(value = 1, message = "User ID must be a non-negative number")
            Long userId
    ) {
        return userMapper.userToUserDTO(userService.getUserById(userId));
    }
}
