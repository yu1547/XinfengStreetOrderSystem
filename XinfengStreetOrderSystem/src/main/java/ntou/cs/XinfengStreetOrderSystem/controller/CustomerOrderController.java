package ntou.cs.XinfengStreetOrderSystem.controller;

import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Corder")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService orderService;

    /**
     * 顯示餐點序號
     * @param id 訂單的唯一 ID
     * @return 訂單序號或 404
     */
    @GetMapping("/number/{id}")
    public ResponseEntity<Integer> getOrderNumber(@PathVariable String id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get().getOrderNumber());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 顯示訂單進度
     * @param id 訂單的唯一 ID
     * @return 訂單進度或 404
     */
    @GetMapping("/status/{id}")
    public ResponseEntity<String> getOrderStatus(@PathVariable String id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get().getOrderStatus());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}