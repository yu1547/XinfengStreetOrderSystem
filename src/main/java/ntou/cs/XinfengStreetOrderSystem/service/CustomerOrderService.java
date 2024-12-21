package ntou.cs.XinfengStreetOrderSystem.service;

import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 根據訂單 ID 查詢訂單
     * @param id 訂單的唯一識別碼
     * @return 訂單資訊（如果存在）
     */
    public Optional<Order> getOrderById(String id) {
        // 使用 OrderRepository 的 findById 方法返回 Optional<Order>
        return orderRepository.findById(id);
    }
}