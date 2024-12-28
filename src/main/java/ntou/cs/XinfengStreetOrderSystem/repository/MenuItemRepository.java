package ntou.cs.XinfengStreetOrderSystem.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    List<MenuItem> findByCategory(String category);

    List<MenuItem> findByNameContainingIgnoreCase(String query);

    Optional<MenuItem> findById(ObjectId objectId);

    List<MenuItem> findByIsAvailableTrue();

    boolean existsByIdAndIsAvailableTrue(String id);

}