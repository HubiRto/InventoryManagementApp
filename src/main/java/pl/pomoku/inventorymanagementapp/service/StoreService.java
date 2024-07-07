package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.CreateStoreRequest;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.exception.StoreAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.StoreNameRequiredException;
import pl.pomoku.inventorymanagementapp.exception.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.UserNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;
import pl.pomoku.inventorymanagementapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public Store createStore(CreateStoreRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.userId());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(request.userId());
        }

        if (storeRepository.existsByNameAndUserId(request.name(), request.userId())) {
            throw new StoreAlreadyExistException();
        }

        User user = optionalUser.get();
        Store store = request.mapToEntity(user);
        return storeRepository.save(store);
    }

    public Store getById(UUID uuid) {
        return storeRepository.findById(uuid).orElseThrow(() -> new StoreNotFoundException(uuid));
    }

    public Set<Store> getAllStoresByUserId(Long id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        return storeRepository.findAllByUserId(id);
    }

    public Store updateName(UUID uuid, String name) {
        if(name == null || name.isBlank()) {
            throw new StoreNameRequiredException();
        }

        Store store = storeRepository.findById(uuid).orElseThrow(() -> new StoreNotFoundException(uuid));
        store.setName(name);
        return storeRepository.save(store);
    }

    @Transactional
    public void deleteById(UUID uuid) {
        Store store = storeRepository.findById(uuid).orElseThrow(() -> new StoreNotFoundException(uuid));
        storeRepository.delete(store);
    }
}
