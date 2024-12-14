package ntou.cs.XinfengStreetOrderSystem.service;

import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RevenueService revenueService;  // 假設您有一個 RevenueService 用來處理營業額

    public OrderService(OrderRepository orderRepository, RevenueService revenueService) {
        this.orderRepository = orderRepository;
        this.revenueService = revenueService;
    }

    // 查詢所有的訂單
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 查詢所有製作中的訂單 (狀態為 accepted)
    public List<Order> getAcceptedOrders() {
        return orderRepository.findByOrderStatus("accepted");
    }

    // 更新訂單狀態為完成
    public void completeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));

        if (!order.getOrderStatus().equals("accepted")) {
            throw new IllegalArgumentException("訂單狀態不允許完成");
        }

        // 更新訂單狀態為完成
        order.setOrderStatus("completed");
        orderRepository.save(order);

        // 更新營業額
        revenueService.updateRevenue(order.getTotalPrice());  // 使用 getTotalPrice() 來獲取訂單金額
    }

    // 接受訂單 (將訂單狀態更新為 accepted)
    public void acceptOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));

        if (!order.getOrderStatus().equals("pending")) {
            throw new IllegalArgumentException("訂單狀態不允許接受");
        }

        order.setOrderStatus("accepted");
        orderRepository.save(order);
    }

    // 拒絕訂單 (將訂單狀態更新為 rejected)
    public void rejectOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));

        // 訂單狀態可以是 "pending" 或 "accepted" 都可以進行拒絕
        if (!order.getOrderStatus().equals("pending") && !order.getOrderStatus().equals("accepted")) {
            throw new IllegalArgumentException("訂單狀態不允許拒絕");
        }

        order.setOrderStatus("rejected");
        orderRepository.save(order);
    }
}
