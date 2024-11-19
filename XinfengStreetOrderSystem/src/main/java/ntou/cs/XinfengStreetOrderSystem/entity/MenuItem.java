package ntou.cs.XinfengStreetOrderSystem.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menuItems")
public class MenuItem {
    @Id
    private String id;
    private String name;
    private String description = "";
    private String image = "";
    private double price;
    private String category = "";
    private boolean isAvailable = true;
    private String setContents = "";

    // Getters and setters omitted for brevity
}
