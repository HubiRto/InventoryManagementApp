package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.ProductAttribute;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    @Query("SELECT COUNT(pa) FROM ProductAttribute pa WHERE pa.attribute.id = :attributeId")
    Long countByAttributeId(Long attributeId);
}
