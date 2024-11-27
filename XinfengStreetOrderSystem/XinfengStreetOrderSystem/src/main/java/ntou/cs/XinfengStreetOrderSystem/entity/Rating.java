package ntou.cs.XinfengStreetOrderSystem.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ratings")
public class Rating {
    @Id
    private String id;
    private String menuItemId;
    private String customerId;
    private int rating;

    // Getters and setters omitted for brevity
}
