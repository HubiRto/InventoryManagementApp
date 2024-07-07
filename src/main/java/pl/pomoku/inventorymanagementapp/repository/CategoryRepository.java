package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Category;
import pl.pomoku.inventorymanagementapp.entity.Store;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByStore(Store store);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.name = :name AND c.store.id = :storeId")
    boolean existsByNameAndStoreId(@Param("name") String name, @Param("storeId") UUID storeId);
}
