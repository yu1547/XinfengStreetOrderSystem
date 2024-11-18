package ntou.cs.XinfengStreetOrderSystem.controller;

import ntou.cs.XinfengStreetOrderSystem.model.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuItemRepository repository;

    // Constructor
    public MenuController(MenuItemRepository repository) {
        this.repository = repository;
    }

    // 獲取所有菜單項目
    @GetMapping
    public ResponseEntity<List<MenuItem>> getMenu() {
        List<MenuItem> menuItems = repository.findAll();
        if (menuItems.isEmpty()) {
            return ResponseEntity.noContent().build();  // 如果沒有菜單項目，返回 204
        }
        return ResponseEntity.ok(menuItems);  // 返回菜單列表
    }

    // 根據ID查詢菜單項目
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable String id) {
        Optional<MenuItem> menuItemOpt = repository.findById(id);
        return menuItemOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());  // 找不到項目時返回404
    }

    // 新增菜單項目
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem item) {
        if (item.getName() == null || item.getDescription() == null) {
            return ResponseEntity.badRequest().build();  // 如果請求體中缺少必填欄位，返回400
        }
        MenuItem savedItem = repository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);  // 返回新增成功的項目
    }

    // 更新菜單項目
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable String id, @RequestBody MenuItem item) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 若找不到ID，返回404
        }
        item.setId(id);
        MenuItem updatedItem = repository.save(item);
        return ResponseEntity.ok(updatedItem);  // 返回更新後的菜單項目
    }

    // 刪除菜單項目
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 若找不到ID，返回404
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();  // 刪除成功，返回204
    }
}
