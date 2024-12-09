package ntou.cs.XinfengStreetOrderSystem.controller;

import ntou.cs.XinfengStreetOrderSystem.service.RevenueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    // 營業額查詢
    @GetMapping("/revenue")
    public double getRevenue() {
        return revenueService.getTotalRevenue(); // 獲取當前的總營業額
    }
}
