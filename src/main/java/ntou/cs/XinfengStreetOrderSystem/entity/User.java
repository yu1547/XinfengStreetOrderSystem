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

    // Getters and setters for User class fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<FavoriteItem> getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(List<FavoriteItem> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }

    // Inner class FavoriteItem
    public static class FavoriteItem {
        private String menuItemId;
        private Date addedAt;

        // Getters and setters for FavoriteItem fields
        public String getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(String menuItemId) {
            this.menuItemId = menuItemId;
        }

        public Date getAddedAt() {
            return addedAt;
        }

        public void setAddedAt(Date addedAt) {
            this.addedAt = addedAt;
        }
    }

    // Inner class OrderHistory
    public static class OrderHistory {
        private String orderId;
        private Date orderedAt;

        // Getters and setters for OrderHistory fields
        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Date getOrderedAt() {
            return orderedAt;
        }

        public void setOrderedAt(Date orderedAt) {
            this.orderedAt = orderedAt;
        }
    }
}
