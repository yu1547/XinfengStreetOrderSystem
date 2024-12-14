package ntou.cs.XinfengStreetOrderSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.entity.Order.OrderItem;
import ntou.cs.XinfengStreetOrderSystem.entity.Order;
import ntou.cs.XinfengStreetOrderSystem.repository.MenuItemRepository;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;

@Service
@SessionScope
public class CartService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    private HttpSession session;

    public CartService(HttpSession session) {
        this.session = session;
    }

    private static final String CART_SESSION_KEY = "cart";

    // 新增品項到暫存的購物車
    public void addItemToCart(String customerId, OrderItem item) {
        if (item.getMenuItemId() == null) {
            throw new IllegalArgumentException("Menu item ID is required");
        }
        // 從資料庫檢查菜單品項是否存在
        MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId()).orElse(null);

        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item not found: " + item.getMenuItemId());
        }
        // 檢查通過後加入購物車
        Order cart = getCart(customerId);

        // 如果購物車中已經有該品項，增加數量
        boolean itemExists = false;
        for (OrderItem orderItem : cart.getItems()) {
            if (orderItem.getMenuItemId().equals(item.getMenuItemId())) {
                orderItem.setQuantity(orderItem.getQuantity() + item.getQuantity());
                itemExists = true;
                break;
            }
        }

        // 如果購物車中沒有該品項，新增品項
        if (!itemExists) {
            cart.getItems().add(item);
        }

        for (OrderItem items : cart.getItems()) {
            System.out.println("Item: " + items.getMenuItemId() + ", Quantity: " + items.getQuantity());
        }

        updateTotalPrice(cart);

        // 更新購物車到 Session
        session.setAttribute(CART_SESSION_KEY, cart);

        // 更新總金額
    }

    // 移除暫存購物車中的品項
    public void removeItemFromCart(String customerId, String menuItemId) {
        Order cart = getCart(customerId);
        cart.getItems().removeIf(item -> item.getMenuItemId().equals(menuItemId));
        updateTotalPrice(cart);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    // 獲取暫存購物車總金額
    public double getTotalPrice(String customerId) {
        Order cart = getCart(customerId);
        return cart.getTotalPrice();
    }

    // 更新總金額
    public void updateTotalPrice(Order cart) {
        int totalPrice = 0;
        for (OrderItem item : cart.getItems()) {
            // 查詢菜單項目
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId()).orElse(null);

            if (menuItem != null) {
                // 獲取價格並計算總價
                totalPrice += menuItem.getPrice() * item.getQuantity();
            } else {
                System.out.println("Menu item not found: " + item.getMenuItemId());
            }
        }
        cart.setTotalPrice(totalPrice);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    // 獲取暫存購物車內容，若沒有則創建一個
    public Order getCart(String customerId) {
        Order cart = (Order) session.getAttribute(CART_SESSION_KEY);
        if (cart == null || (cart.getCustomerId() == null || !cart.getCustomerId().equals(customerId))) {
            cart = new Order(customerId, new ArrayList<>(), 0, "Pending", null, 0, null);
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        System.out.println("Cart for " + customerId + ": " + cart);
        return cart;
    }

    // 清空暫存購物車
    public void clearCart(String customerId) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}
