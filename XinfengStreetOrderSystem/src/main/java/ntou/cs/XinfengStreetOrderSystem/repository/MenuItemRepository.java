package ntou.cs.XinfengStreetOrderSystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
}
