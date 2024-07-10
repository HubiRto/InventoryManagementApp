package pl.pomoku.inventorymanagementapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @Secured("ADMIN")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUser().stream()
                .map(userMapper::userToUserDTO)
                .toList();
    }

    @Secured("ADMIN")
    @GetMapping("{userId}")
    public UserDTO getUserById(
            @NotNull(message = "User ID cannot be null")
            @Min(value = 1, message = "User ID must be a non-negative number")
            @PathVariable("userId") Long userId
    ) {
        return userMapper.userToUserDTO(userService.getUserById(userId));
    }

    @Secured("ADMIN")
    @GetMapping
    public UserDTO getUserByEmail(
            @NotNull(message = "User email cannot be null")
            @NotEmpty(message = "User email cannot be empty")
            @Email(message = "Invalid email format")
            @RequestParam("email") String email
    ) {
        return userMapper.userToUserDTO(userService.getUserByEmail(email));
    }
}
