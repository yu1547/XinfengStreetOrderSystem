package ntou.cs.XinfengStreetOrderSystem.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String name;
    private int age;
    private String gender;
    private String email;
    private String role;
    private List<FavoriteItem> favoriteItems;
    private List<OrderHistory> orderHistory;

    // Getters and setters omitted for brevity

    public static class FavoriteItem {
        private String menuItemId;
        private Date addedAt;

        // Getters and setters omitted for brevity
    }

    public static class OrderHistory {
        private String orderId;
        private Date orderedAt;

        // Getters and setters omitted for brevity
    }
}
