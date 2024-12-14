package ntou.cs.XinfengStreetOrderSystem.service;

import ntou.cs.XinfengStreetOrderSystem.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DailyTaskService {

    private final OrderRepository orderRepository;
    private final RevenueService revenueService;  // 注入 RevenueService

    public DailyTaskService(OrderRepository orderRepository, RevenueService revenueService) {
        this.orderRepository = orderRepository;
        this.revenueService = revenueService;
    }

    // 每日 0 點清空
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyData() {
        orderRepository.deleteAll(); // 刪除所有訂單
        revenueService.resetRevenue();  // 重置營業額
        System.out.println("每日清空任務已執行: 訂單已清空, 營業額已重置");
    }
}
