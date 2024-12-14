package ntou.cs.XinfengStreetOrderSystem.repository;

import ntou.cs.XinfengStreetOrderSystem.entity.Revenue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevenueRepository extends MongoRepository<Revenue, String> {
    Revenue findByDate(String date); // 根據日期查詢營業額
}
