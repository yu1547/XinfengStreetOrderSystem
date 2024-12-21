package ntou.cs.XinfengStreetOrderSystem.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import ntou.cs.XinfengStreetOrderSystem.entity.Rating;
import ntou.cs.XinfengStreetOrderSystem.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @CrossOrigin(origins = "http://localhost:3000")
    // API: 獲取所有評分
    @GetMapping
    public ResponseEntity<List<Rating>> getRatings() {
        try {
            List<Rating> ratings = ratingService.getRatings();
            return ResponseEntity.ok(ratings);
        } catch (Exception e) {
            System.err.println("Error fetching ratings: " + e.getMessage());
            return ResponseEntity.status(500).body(null); // 若查詢失敗則返回500錯誤
        }
    }

    // API: 添加評分
    @PostMapping("/api/ratings") // 修改路徑，以便接收多筆評分
public ResponseEntity<Map<String, String>> addRatings(@RequestBody List<Rating> ratings) {
    try {
        boolean isSuccess = ratingService.addRatings(ratings); // 調用服務層新增多筆評分
        Map<String, String> response = new HashMap<>();
        if (isSuccess) {
            response.put("message", "Ratings added successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to add ratings");
            return ResponseEntity.status(400).body(response);
        }
    } catch (Exception e) {
        System.err.println("Error adding ratings: " + e.getMessage());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error: " + e.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}
    // API: 計算並更新餐點平均評分
    @GetMapping("/api/average/{menuItemId}")
    public ResponseEntity<String> updateAverageRating(@PathVariable String menuItemId) {
        try {
            ratingService.calculateAndUpdateAverageRating(menuItemId);
            return ResponseEntity.ok("Average rating updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Menu item not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error updating average rating: " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // API: 查詢某餐點的平均評分
    @GetMapping("/menu/{menuItemId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable String menuItemId) {
        try {
            double averageRating = ratingService.getAverageRating(menuItemId);
            return ResponseEntity.ok(averageRating);
        } catch (Exception e) {
            System.err.println("Error fetching average rating: " + e.getMessage());
            return ResponseEntity.status(500).body(null); // 返回500錯誤
        }
    }
}
