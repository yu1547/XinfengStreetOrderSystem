package ntou.cs.XinfengStreetOrderSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.entity.Order.OrderItem;
import ntou.cs.XinfengStreetOrderSystem.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    //獲取暫存的購物車內容
    @GetMapping("/{customerId}")
    public ResponseEntity<Order> getCart(@PathVariable String customerId) {
        Order cart = cartService.getCart(customerId);
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cart);
    }

    //新增品項到購物車
    @PostMapping("/{customerId}/add")
    public ResponseEntity<Void> addItemToCart(@PathVariable String customerId, @RequestBody OrderItem item) {
        try {
            cartService.addItemToCart(customerId, item);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // 打印堆棧跟蹤信息
            return ResponseEntity.status(500).build(); // 返回 500 錯誤
        }
    }

    //移除購物車中的品項
    @DeleteMapping("/{customerId}/remove/{menuItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable String customerId, @PathVariable String menuItemId) {
        cartService.removeItemFromCart(customerId, menuItemId);
        return ResponseEntity.ok().build();
    }

    // 獲取總金額
    @GetMapping("/{customerId}/total")
    public ResponseEntity<Double> getTotal(@PathVariable String customerId) {
        double totalPrice = cartService.getTotalPrice(customerId);
        return ResponseEntity.ok(totalPrice);
    }

    //提交購物車內容
    @PostMapping("/{customerId}/submit")
    public ResponseEntity<Order> submitOrder(@PathVariable String customerId) {
        Order cart = cartService.getCart(customerId);

        // 如果購物車為空，返回錯誤
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 清空購物車並返回提交的訂單內容
        cartService.clearCart(customerId);

        return ResponseEntity.ok(cart);
    }
}