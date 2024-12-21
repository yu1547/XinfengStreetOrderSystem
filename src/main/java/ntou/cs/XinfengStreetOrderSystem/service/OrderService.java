package ntou.cs.XinfengStreetOrderSystem.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.entity.User;
import ntou.cs.XinfengStreetOrderSystem.repository.OrderRepository;
import ntou.cs.XinfengStreetOrderSystem.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, MailService mailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    // 查詢所有的訂單
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // 查詢所有製作中的訂單 (狀態為 accepted)
    public List<Order> getAcceptedOrders() {
        return orderRepository.findByOrderStatus("accepted");
    }

    // 更新訂單狀態為完成並發送郵件通知
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

        // 通知客戶
        notifyCustomer(order.getCustomerId(), "您的訂單已完成，請在三十分鐘內前來取餐!");
    }

    // 接受訂單並發送郵件通知
    public void acceptOrder(String orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));
    
            if (!order.getOrderStatus().equals("pending")) {
                throw new IllegalArgumentException("訂單狀態不允許接受");
            }
    
            order.setOrderStatus("accepted");
            order.setStatusUpdatedAt(new Date()); // 更新時間
            orderRepository.save(order);
    
            // 通知客戶
            notifyCustomer(order.getCustomerId(), "您的訂單已被接受，將於完成後通知您!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("訂單接受失敗: " + e.getMessage());
        }
    }
    

    // 拒絕訂單並發送郵件通知
    public void rejectOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));

        if (!order.getOrderStatus().equals("pending") && !order.getOrderStatus().equals("accepted")) {
            throw new IllegalArgumentException("訂單狀態不允許拒絕");
        }

        order.setOrderStatus("rejected");
        order.setStatusUpdatedAt(new Date()); // 更新時間
        orderRepository.save(order);

        // 通知客戶
        notifyCustomer(order.getCustomerId(), "您的訂單已被拒絕，請確認訂單內是否存在已售完的商品或是其他問題後再次送出訂單!");
    }

    // 根據customerId通知客戶
    private void notifyCustomer(String customerId, String message) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("用戶不存在"));

        String email = user.getEmail();
        if (email != null && !email.isEmpty()) {
            mailService.sendOrderNotificationEmail(email, message);
        } else {
            throw new IllegalArgumentException("用戶沒有註冊電子郵件");
        }
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
