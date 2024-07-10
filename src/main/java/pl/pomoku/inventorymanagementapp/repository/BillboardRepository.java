package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Billboard;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillboardRepository extends JpaRepository<Billboard, Long> {
    List<Billboard> findAllByStoreId(Long storeId);
    boolean existsByLabel(String label);
}
