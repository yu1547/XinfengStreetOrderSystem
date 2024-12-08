package ntou.cs.XinfengStreetOrderSystem.controller;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.exception.ResourceNotFoundException;
import ntou.cs.XinfengStreetOrderSystem.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
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
    @GetMapping("/id/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable String id) {
        try {
            MenuItem menuItem = menuService.getMenuById(id); // 根據 ID 查找菜單項目
            return ResponseEntity.ok(menuItem);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // 如果找不到菜單，返回 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String id) {
        if (!menuService.existsById(id)) {  // 檢查菜單項目是否存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 如果找不到該菜單項目，返回 404
        }
        menuService.deleteMenuItem(id);  // 刪除菜單項目
        return ResponseEntity.noContent().build();  // 刪除成功，返回 204 No Content
    }
     // 新增餐點
     @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestParam String name,
                                                @RequestParam String description,
                                                @RequestParam Double price,
                                                @RequestParam String setContents,
                                                @RequestParam String category,
                                                
                                                @RequestParam MultipartFile image) throws IOException {
     if (image.isEmpty()) {
      return ResponseEntity.badRequest().body(null); // 可以返回錯誤響應
    }
                                                
                                                    // 呼叫服務層處理圖片儲存及其他邏輯
     MenuItem newMenuItem = menuService.addMenuItem(name, description, price, setContents,category,image);
                                                
     return ResponseEntity.ok(newMenuItem);
}

       // 修改餐點資訊
       @PutMapping("/{id}")
       public ResponseEntity<MenuItem> updateMenuItem(@PathVariable String id,
                                                    @RequestParam String name,
                                                    @RequestParam String description,
                                                    @RequestParam Double price,
                                                    @RequestParam String setContents,
                                            
                                                    @RequestParam String category,
                                                    
                                                    @RequestParam(required = false) MultipartFile image)throws IOException {
           try {
            MenuItem  updatedMenuItem =  menuService.updateMenuItem(id, name, description, price, setContents,category, image);
            return ResponseEntity.ok(updatedMenuItem);
       }   catch (ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
   
 
}
}