package ntou.cs.XinfengStreetOrderSystem.controller;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    //獲取所有菜單項目
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        if (menuItems.isEmpty()) {
            return ResponseEntity.noContent().build();  // 如果沒有菜單項目，返回 204
        }
        return ResponseEntity.ok(menuItems);
    }

    //關鍵字搜尋菜單項目
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItems(@RequestParam String query) {
        List<MenuItem> results = menuService.searchMenuItems(query);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    // 按類別列出菜單項目
    @GetMapping("/{category}")
    public ResponseEntity<List<MenuItem>> getMenuByCategory(@PathVariable String category) {
        List<MenuItem> items = menuService.getMenuItemsByCategory(category);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String id) {
        if (!menuService.existsById(id)) {  // 檢查菜單項目是否存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 如果找不到該菜單項目，返回 404
        }
        menuService.deleteMenuItem(id);  // 刪除菜單項目
        return ResponseEntity.noContent().build();  // 刪除成功，返回 204 No Content
    }
}
