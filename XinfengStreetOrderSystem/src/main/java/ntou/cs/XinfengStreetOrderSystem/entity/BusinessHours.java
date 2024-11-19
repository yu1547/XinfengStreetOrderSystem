package ntou.cs.XinfengStreetOrderSystem.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "businessHours")
public class BusinessHours {
    @Id
    private String id;
    private String dayOfWeek;
    private String openTime;
    private String closeTime;
    private int minimumOrderLeadTime;

    // Getters and setters omitted for brevity
}
