package ntou.cs.XinfengStreetOrderSystem.repository;

import ntou.cs.XinfengStreetOrderSystem.entity.Order; // 確保導入 Order 類別
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}
