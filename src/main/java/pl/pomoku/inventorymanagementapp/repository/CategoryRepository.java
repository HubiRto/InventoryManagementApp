package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.name = :name AND c.store.id = :storeId")
    boolean existsByNameAndStoreId(@Param("name") String name, @Param("storeId") Long storeId);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.store.id = :storeId")
    List<Category> findAllWithoutParentByStoreId(@Param("storeId") Long storeId);

    List<Category> findAllByStoreId(Long storeId);
}
