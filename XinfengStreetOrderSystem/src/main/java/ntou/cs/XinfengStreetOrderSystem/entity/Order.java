package ntou.cs.XinfengStreetOrderSystem.entity;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerId;
    private List<OrderItem> items;
    private double totalPrice;
    private String orderStatus = "pending";
    private Date pickupTime;
    private int orderNumber;
    private Date statusUpdatedAt = new Date();

    // Getters and setters omitted for brevity

    public static class OrderItem {
        private String menuItemId;
        private int quantity;

        // Getters and setters omitted for brevity
    }
}
