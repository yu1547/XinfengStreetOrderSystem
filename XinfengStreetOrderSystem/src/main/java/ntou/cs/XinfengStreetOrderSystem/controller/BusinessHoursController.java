package ntou.cs.XinfengStreetOrderSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ntou.cs.XinfengStreetOrderSystem.entity.BusinessHours;
import ntou.cs.XinfengStreetOrderSystem.service.BusinessHoursService;

@RestController
@RequestMapping("/api/business-hours")
public class BusinessHoursController {

    @Autowired
    private BusinessHoursService businessHoursService;

    // 取得營業時間資料
    @GetMapping
    public ResponseEntity<BusinessHours> getBusinessHours() {
        try {
            BusinessHours businessHours = businessHoursService.getBusinessHours();
            return ResponseEntity.ok(businessHours);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 發生錯誤時回傳 500 錯誤
        }
    }
}
