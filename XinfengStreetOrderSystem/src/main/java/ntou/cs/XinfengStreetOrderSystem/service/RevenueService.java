package ntou.cs.XinfengStreetOrderSystem.service;

import ntou.cs.XinfengStreetOrderSystem.entity.Revenue;
import ntou.cs.XinfengStreetOrderSystem.repository.RevenueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RevenueService {

    private final RevenueRepository revenueRepository;

    public RevenueService(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    public void updateRevenue(double orderAmount) {
        // 獲取今天的日期
        String today = LocalDate.now().toString();

        // 檢查今天的營業額是否已存在
        Revenue revenue = revenueRepository.findByDate(today);
        if (revenue == null) {
            // 如果不存在，創建新的 Revenue 資料
            revenue = new Revenue();
            revenue.setDate(today);
            revenue.setTotalAmount(orderAmount);
        } else {
            // 如果存在，更新 totalAmount
            revenue.setTotalAmount(revenue.getTotalAmount() + orderAmount);
        }

        // 儲存到資料庫
        revenueRepository.save(revenue);
        System.out.println("營業額更新: " + revenue.getTotalAmount());
    }

    public double getTotalRevenue() {
        // 查詢今天的營業額
        String today = LocalDate.now().toString();
        Revenue revenue = revenueRepository.findByDate(today);
        return (revenue != null) ? revenue.getTotalAmount() : 0.0;
    }

    public void resetRevenue() {
        String today = LocalDate.now().toString();
        Revenue revenue = revenueRepository.findByDate(today);
        if (revenue != null) {
            revenue.setTotalAmount(0.0);
            revenueRepository.save(revenue);
        }
        System.out.println("今日營業額已重置");
    }
}
