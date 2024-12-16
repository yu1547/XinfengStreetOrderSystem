package ntou.cs.XinfengStreetOrderSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntou.cs.XinfengStreetOrderSystem.entity.BusinessHours;
import ntou.cs.XinfengStreetOrderSystem.repository.BusinessHoursRepository;

@Service
public class BusinessHoursService {

    @Autowired
    private BusinessHoursRepository businessHoursRepository;

    // 取得營業時間資料
    public BusinessHours getBusinessHours() {
        // 假設只有一筆資料，這裡根據 id 查詢
        // 你可以根據需求調整，若資料有多筆，可以進行其他處理
        return businessHoursRepository.findById("67609b0fa8436be23349956b")
            .orElseThrow(() -> new RuntimeException("Business hours not found"));
    }
}
