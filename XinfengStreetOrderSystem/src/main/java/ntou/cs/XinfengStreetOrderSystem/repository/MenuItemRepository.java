package ntou.cs.XinfengStreetOrderSystem.repository;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByNameContainingIgnoreCase(String query);
}

