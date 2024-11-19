package ntou.cs.XinfengStreetOrderSystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository; // 確保導入 BusinessHours 類別

import ntou.cs.XinfengStreetOrderSystem.entity.BusinessHours;

public interface BusinessHoursRepository extends MongoRepository<BusinessHours, String> {
}
