package ntou.cs.XinfengStreetOrderSystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ntou.cs.XinfengStreetOrderSystem.entity.BusinessHours;

@Repository
public interface BusinessHoursRepository extends MongoRepository<BusinessHours, String> {
    // 這邊如果需要額外的查詢方法，可以在這裡擴充
}
