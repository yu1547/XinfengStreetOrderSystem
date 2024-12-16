package ntou.cs.XinfengStreetOrderSystem.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "businessHours")
public class BusinessHours {
    @Id
    private String id;
    private List<String> dayOfWeek;  // 將 dayOfWeek 修改為 List<String>，用於表示星期幾開放
    private String openTime;
    private String closeTime;
    private int minimumOrderLeadTime;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(List<String> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getMinimumOrderLeadTime() {
        return minimumOrderLeadTime;
    }

    public void setMinimumOrderLeadTime(int minimumOrderLeadTime) {
        this.minimumOrderLeadTime = minimumOrderLeadTime;
    }
}
