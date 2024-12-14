package ntou.cs.XinfengStreetOrderSystem.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String customerId;
    private List<OrderItem> items;
    private double totalPrice;
    private String orderStatus="pending";
    private Date pickupTime;
    private int orderNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")

    private Date statusUpdatedAt=new Date();

    // Constructor, Getters and Setters
    public Order(String customerId, List<OrderItem> items, double totalPrice, String orderStatus, Date pickupTime, int orderNumber, Date statusUpdatedAt) {
        this.customerId = customerId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.pickupTime = pickupTime;
        this.orderNumber = orderNumber;
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public static class OrderItem {
        private String menuItemId;
        private int quantity;
        
        public OrderItem(String menuItemId, int quantity) {
            this.menuItemId = menuItemId;
            this.quantity = quantity;
        }
    
        public String getMenuItemId() {
            return menuItemId;
        }
    
        public void setMenuItemId(String menuItemId) {
            this.menuItemId = menuItemId;
        }
    
        public int getQuantity() {
            return quantity;
        }
    
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public void setStatusUpdatedAt(Date statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }
}
