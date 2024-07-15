package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Producent;
import pl.pomoku.inventorymanagementapp.entity.Store;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProducentRepository extends JpaRepository<Producent, Long> {
    List<Producent> findAllByStore(Store store);
    boolean existsByName(String name);
}
