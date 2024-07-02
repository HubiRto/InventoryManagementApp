package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pomoku.inventorymanagementapp.dto.response.UserDto;
import pl.pomoku.inventorymanagementapp.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByToken(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(userService.findUserByToken(token).mapToDto(), HttpStatus.OK);
    }
}
