package ntou.cs.XinfengStreetOrderSystem.service;

import ntou.cs.XinfengStreetOrderSystem.entity.Rating;
import ntou.cs.XinfengStreetOrderSystem.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    // 獲取所有評分
    public List<Rating> getRatings() {
        try {
            return ratingRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error fetching ratings: " + e.getMessage());
            return null; // 若查詢失敗，返回 null
        }
    }

    // 新增評分
    public boolean addRating(Rating rating) {
        try {
            // 儲存評分到資料庫
            ratingRepository.save(rating);

            // 即時更新該餐點的平均評分
            calculateAndUpdateAverageRating(rating.getMenuItemId());
            return true;
        } catch (Exception e) {
            System.err.println("Error adding rating: " + e.getMessage());
            return false;
        }
    }

    // 計算並更新平均評分
    public void calculateAndUpdateAverageRating(String menuItemId) {
        // 查詢該餐點的所有評分
        List<Rating> ratings = ratingRepository.findByMenuItemId(menuItemId);
        if (ratings == null || ratings.isEmpty()) {
            throw new IllegalArgumentException("No ratings found for menu item ID: " + menuItemId);
        }

        // 計算平均評分
        double totalRating = ratings.stream()
                .mapToDouble(Rating::getRating)
                .sum();
        double averageRating = totalRating / ratings.size();

        // 更新所有相關評分的平均分數
        for (Rating rating : ratings) {
            rating.setAverageRating(averageRating); // 假設新增了 `averageRating` 欄位
            ratingRepository.save(rating);         // 儲存更新
        }
    }

    // 查詢某餐點的平均評分
    public double getAverageRating(String menuItemId) {
        List<Rating> ratings = ratingRepository.findByMenuItemId(menuItemId);
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double totalRating = ratings.stream()
                .mapToDouble(Rating::getRating)
                .sum();
        return totalRating / ratings.size();
    }
}
