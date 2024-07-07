package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.entity.ProductImage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillboardRepository extends JpaRepository<Billboard, UUID> {
    List<Billboard> findAllByStoreId(UUID storeId);
}
