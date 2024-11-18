package ntou.cs.XinfengStreetOrderSystem.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ntou.cs.XinfengStreetOrderSystem.model.MenuItem;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    // 查詢菜單名稱的方法（如果需要的話）
    List<MenuItem> findByName(String name);
}
