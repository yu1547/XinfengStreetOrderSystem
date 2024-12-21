package ntou.cs.XinfengStreetOrderSystem.repository;

import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByOrderStatus(String orderStatus); // 查詢指定狀態的訂單
}
