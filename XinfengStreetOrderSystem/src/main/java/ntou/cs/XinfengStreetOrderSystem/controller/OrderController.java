package ntou.cs.XinfengStreetOrderSystem.controller;

import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // API 1: 查詢製作清單 (狀態為 accepted 的訂單)
    @GetMapping("/accepted")
    public ResponseEntity<List<Order>> getAcceptedOrders() {
        List<Order> orders = orderService.getAcceptedOrders();
        return ResponseEntity.ok(orders);
    }

    // API 2: 完成特定訂單
    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestParam String orderId) {
        try {
            orderService.completeOrder(orderId);
            return ResponseEntity.ok("{\"message\": \"訂單已完成\"}"); // 使用 JSON 格式的響應
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"無法完成訂單: " + e.getMessage() + "\"}");
        }
    }

    // API 3: 查詢所有訂單
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // API 4: 接受訂單
    @PostMapping("/accept")
    public ResponseEntity<String> acceptOrder(@RequestParam String orderId) {
        try {
            orderService.acceptOrder(orderId);
            return ResponseEntity.ok("{\"message\": \"訂單已接受\"}"); // 使用 JSON 格式的響應
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"無法接受訂單: " + e.getMessage() + "\"}");
        }
    }

    // API 5: 拒絕訂單
    @PostMapping("/reject")
    public ResponseEntity<String> rejectOrder(@RequestParam String orderId) {
        try {
            orderService.rejectOrder(orderId);
            return ResponseEntity.ok("{\"message\": \"訂單已拒絕\"}"); // 使用 JSON 格式的響應
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"無法拒絕訂單: " + e.getMessage() + "\"}");
        }
    }
}
