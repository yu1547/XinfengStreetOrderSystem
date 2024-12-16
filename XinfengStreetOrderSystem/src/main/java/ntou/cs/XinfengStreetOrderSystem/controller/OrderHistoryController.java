package ntou.cs.XinfengStreetOrderSystem.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.entity.User;
import ntou.cs.XinfengStreetOrderSystem.repository.MenuItemRepository;
import ntou.cs.XinfengStreetOrderSystem.repository.OrderRepository;
import ntou.cs.XinfengStreetOrderSystem.service.UserService;

@RestController
@RequestMapping("/api/orderHistory")
public class OrderHistoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;  // 用來查詢 MenuItem

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDetails>> getOrderHistory(@PathVariable String userId) {
        System.out.println("Request received for userId: " + userId);
        Optional<User> user = userService.findUserById(userId);
        if (user.isPresent()) {
            List<User.OrderHistory> orderHistory = user.get().getOrderHistory();

            // 使用 orderId 查詢訂單詳細資料並帶入菜單名稱、價格
            List<OrderDetails> detailedOrders = orderHistory.stream()
                .map(orderHist -> {
                    Optional<Order> order = orderRepository.findById(orderHist.getOrderId());
                    return order.map(o -> {
                        // 計算總金額
                        double totalPrice = o.getItems().stream()
                            .mapToDouble(item -> {
                                Optional<MenuItem> menuItem = menuItemRepository.findById(item.getMenuItemId());
                                return menuItem.map(mi -> mi.getPrice() * item.getQuantity()).orElse(0.0);
                            })
                            .sum();

                        // 替換 menuItem 的名稱、價格及套餐內容
                        List<OrderItemDetails> itemDetails = o.getItems().stream()
                            .map(item -> {
                                Optional<MenuItem> menuItem = menuItemRepository.findById(item.getMenuItemId());
                                if (menuItem.isPresent()) {
                                    MenuItem mi = menuItem.get();
                                    String setContents = mi.getSetContents().isEmpty() ? "" : "<span class='set-content'>" + mi.getSetContents() + "</span>";
                                    return new OrderItemDetails(mi.getName(), mi.getPrice(), item.getQuantity(), setContents);
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                        return new OrderDetails(
                            o.getId(),
                            o.getOrderNumber(),
                            itemDetails,
                            o.getOrderStatus(),
                            o.getPickupTime(),
                            orderHist.getOrderedAt(),
                            totalPrice,
                            o.getNotes()
                        );
                    }).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            return ResponseEntity.ok(detailedOrders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DTO 類別，用於返回訂單的詳細資訊
    public static class OrderDetails {
        private String orderId;
        private int orderNumber;
        private List<OrderItemDetails> items;
        private String orderStatus;
        private Date pickupTime;
        private Date orderedAt;
        private double totalPrice;
        private String notes;

        public OrderDetails(String orderId, int orderNumber, List<OrderItemDetails> items, String orderStatus, Date pickupTime, Date orderedAt, double totalPrice,String notes) {
            this.orderId = orderId;
            this.orderNumber = orderNumber;
            this.items = items;
            this.orderStatus = orderStatus;
            this.pickupTime = pickupTime;
            this.orderedAt = orderedAt;
            this.totalPrice = totalPrice;
            this.notes = notes;
        }

        // Getters and Setters
        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public List<OrderItemDetails> getItems() {
            return items;
        }

        public void setItems(List<OrderItemDetails> items) {
            this.items = items;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public String getNotes() {
            return notes;
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

        public Date getOrderedAt() {
            return orderedAt;
        }

        public void setOrderedAt(Date orderedAt) {
            this.orderedAt = orderedAt;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }
    }

    public static class OrderItemDetails {
        private String name;
        private double price;
        private int quantity;
        private String setContents;

        public OrderItemDetails(String name, double price, int quantity, String setContents) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.setContents = setContents;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getSetContents() {
            return setContents;
        }

        public void setSetContents(String setContents) {
            this.setContents = setContents;
        }
    }
}
