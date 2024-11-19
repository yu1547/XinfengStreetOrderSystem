package ntou.cs.XinfengStreetOrderSystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository; // 確保導入 User 類別

import ntou.cs.XinfengStreetOrderSystem.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
}
