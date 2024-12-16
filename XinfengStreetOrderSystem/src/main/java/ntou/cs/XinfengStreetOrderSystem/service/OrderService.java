package ntou.cs.XinfengStreetOrderSystem.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
        order.setStatusUpdatedAt(new Date()); // 更新時間
        orderRepository.save(order);
    }

    // 接受訂單 (將訂單狀態更新為 accepted)
    public void acceptOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));

        if (!order.getOrderStatus().equals("pending")) {
            throw new IllegalArgumentException("訂單狀態不允許接受");
        }

        order.setOrderStatus("accepted");
        order.setStatusUpdatedAt(new Date()); // 更新時間
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
        order.setStatusUpdatedAt(new Date()); // 更新時間
        orderRepository.save(order);
    }

    // 清除所有訂單
    public void clearAllOrders() {
        orderRepository.deleteAll();
    }



   // 根據訂單 ID 查詢訂單
public Order getOrderById(String orderId) {
    return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));
}

// 根據資料庫中的訂單數量生成下一個訂單號碼
public int getNextOrderNumber() {
    long count = orderRepository.count();  // 取得資料庫中的訂單數量
    return (int) (count + 1);  // 生成新的訂單號碼，從 1 開始
}
    // 其他方法...
}




