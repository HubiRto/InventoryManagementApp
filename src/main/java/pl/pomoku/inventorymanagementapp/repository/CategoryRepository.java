package pl.pomoku.inventorymanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pomoku.inventorymanagementapp.entity.Category;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT DISTINCT name FROM Category", nativeQuery = true)
    Set<String> findDistinctNames();
    boolean existsByName(String name);
    void deleteByName(String name);
}
