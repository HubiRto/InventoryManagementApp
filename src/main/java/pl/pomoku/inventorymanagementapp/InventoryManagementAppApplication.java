package pl.pomoku.inventorymanagementapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.pomoku.inventorymanagementapp.entity.Event;
import pl.pomoku.inventorymanagementapp.entity.Role;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.enumerated.EventType;
import pl.pomoku.inventorymanagementapp.repository.EventRepository;
import pl.pomoku.inventorymanagementapp.repository.RoleRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;
import pl.pomoku.inventorymanagementapp.repository.UserRepository;
import pl.pomoku.inventorymanagementapp.service.JwtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
public class InventoryManagementAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner clr(
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtService jwtService,
            StoreRepository storeRepository,
            EventRepository eventRepository
    ) {
        return args -> {
            //Generates roles (user, admin) if not exist
            List<String> roleNames = List.of("USER", "ADMIN");
            Set<Role> roles = roleNames.stream()
                    .map(name -> roleRepository.findByName(name)
                            .orElseGet(() -> roleRepository.save(new Role(name))))
                    .collect(Collectors.toSet());

            //Generate user if not exist
            User user = userRepository.findByEmail("hubert.rybicki.hr@gmail.com")
                    .orElseGet(() -> {
                        log.info("Admin user Hubert Rybicki added");
                        return userRepository.save(User.builder()
                                .firstName("Hubert")
                                .lastName("Rybicki")
                                .email("hubert.rybicki.hr@gmail.com")
                                .password("$2a$10$EctaXMtY8uE8lYLyMSauyeZ.MsVkwNAg39xiNXM3IiBXxVuwfr8OK")
                                .roles(roles)
                                .isEnabled(true)
                                .isAccountNonExpired(true)
                                .isCredentialsNonExpired(true)
                                .isAccountNonLocked(true)
                                .createdAt(LocalDateTime.now())
                                .build()
                        );
                    });

            //Generate jwt token for admin user but don't save it in database
            String token = jwtService.generateToken(user);
            log.info("JWT token: {}", token);

            //Generate basic store if not exist
            Store store = storeRepository.findByName("Budex")
                    .orElseGet(() -> {
                        eventRepository.save(new Event(
                                EventType.CREATE,
                                "Admin (Hubert Rybicki) create store with name (Budex)"
                        ));
                        log.info("Create store with name (Budex)");
                        log.info("Create store event registered");

                        return Store.builder()
                                .name("Budex")
                                .user(user)
                                .createdAt(LocalDateTime.now())
                                .build();

                    });
        };
    }
}
