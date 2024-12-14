package ntou.cs.XinfengStreetOrderSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ntou.cs.XinfengStreetOrderSystem.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
    
    // 根據用戶名查找
    User findByUsername(String username);
    
    // 根據角色查找
    List<User> findByRole(String role);
    
    // 根據郵件查找
    Optional<User> findByEmail(String email);
    
    // 查詢收藏某菜單項的用戶
    @Query("{'favoriteItems.menuItemId': ?0}")
    List<User> findByFavoriteMenuItem(String menuItemId);
    
    // 分頁查找角色用戶
    Page<User> findByRole(String role, Pageable pageable);
}



