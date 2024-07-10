package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Store;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByNameAndUserId(String name, Long userId);
    boolean existsByName(String name);
    Set<Store> findAllByUserId(Long userId);
    Optional<Store> findByName(String name);
}
