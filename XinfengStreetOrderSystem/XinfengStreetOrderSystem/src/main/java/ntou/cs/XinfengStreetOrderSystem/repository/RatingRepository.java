package ntou.cs.XinfengStreetOrderSystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository; 

import ntou.cs.XinfengStreetOrderSystem.entity.Rating;// 確保導入 Rating 類別
public interface RatingRepository extends MongoRepository<Rating, String> {
}
