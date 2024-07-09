package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Producent;

@Repository
public interface ProducentRepository extends JpaRepository<Producent, Long> {
}
