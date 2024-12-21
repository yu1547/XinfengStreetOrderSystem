package ntou.cs.XinfengStreetOrderSystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ntou.cs.XinfengStreetOrderSystem.entity.Rating;
import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    // 根據 menuItemId 查詢評分
    List<Rating> findByMenuItemId(String menuItemId);

    // 根據 customerId 查詢評分
    List<Rating> findByCustomerId(String customerId);
}
