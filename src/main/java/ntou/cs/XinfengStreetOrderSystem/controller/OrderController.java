package ntou.cs.XinfengStreetOrderSystem.controller;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.repository.OrderRepository;
import ntou.cs.XinfengStreetOrderSystem.service.BusinessHoursService;
import ntou.cs.XinfengStreetOrderSystem.service.OrderService;
import ntou.cs.XinfengStreetOrderSystem.service.UserService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserService userService; 
    private BusinessHoursService businessHoursService;
    // 建構函數注入 OrderService 和 OrderRepository
    @Autowired
    public OrderController(OrderService orderService, OrderRepository orderRepository,UserService userService,BusinessHoursService businessHoursService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.userService = userService; 
        this.businessHoursService= businessHoursService;
        
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


// API 6: 清除所有訂單
@PostMapping("/clear")
public ResponseEntity<String> clearAllOrders() {
    try {
        orderService.clearAllOrders();  // 這裡調用服務層的清除方法
        return ResponseEntity.ok("{\"message\": \"所有訂單已清除\"}");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("{\"error\": \"無法清除訂單: " + e.getMessage() + "\"}");
    }
}


    // 根據訂單 ID 獲取訂單詳細資料
    @GetMapping("/{orderId}")
    public Order getOrderDetails(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }
    // 提交訂單
    @PostMapping
    public ResponseEntity<String> submitOrder(@RequestBody Order order,HttpSession session) {
        // 從 session 中獲取當前用戶的 ID 
        String customerId = (String) session.getAttribute("loggedInUser");
        if (customerId == null) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用戶未登入");}

        order.setCustomerId(customerId);
        order.setOrderNumber(orderService.getNextOrderNumber());
        order.setStatusUpdatedAt(new Date());
        order.setOrderStatus("待接受");
        System.out.println("Order note: " + order.getNotes());
        order = orderRepository.save(order);
        // 確保訂單 ID 已經成功生成
        String orderId = order.getId();

        // 呼叫 UserService 更新訂單歷史
        userService.updateOrderHistory(customerId, orderId);
        
        System.out.println("Order saved: " + orderId);  // 這行可以確認 order 是否被保存
        return ResponseEntity.ok(orderId);

        }

}




