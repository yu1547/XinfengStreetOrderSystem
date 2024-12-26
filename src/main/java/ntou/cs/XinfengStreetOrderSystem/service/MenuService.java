package ntou.cs.XinfengStreetOrderSystem.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.repository.MenuItemRepository;

@Service
public class MenuService {
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private FileStorageService fileStorageService; // 新增圖片處理服務

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> searchMenuItems(String query) {
        return menuItemRepository.findByNameContainingIgnoreCase(query);
    }

    public List<String> getAllCategories() {
        // 獲取所有菜單項目
        List<MenuItem> menuItems = menuItemRepository.findAll();

        // 使用 List 去除重複的類別
        List<String> categories = new ArrayList<>();
        for (MenuItem item : menuItems) {
            String category = item.getCategory();
            // 如果類別不在列表中，則加入
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }

        return categories;
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    // 檢查是否存在菜單項目
    public boolean existsById(String id) {
        return menuItemRepository.existsById(id); // 根據 ID 查找是否存在菜單項目
    }

    // 刪除菜單項目
    public void deleteMenuItem(String id) {
        menuItemRepository.deleteById(id); // 根據 ID 刪除菜單項目
    }

   public MenuItem addMenuItem(String name, String description, Double price, String setContents, String category, String imageUrl) throws IOException {
        List<MenuItem> existingMenuItem = menuItemRepository.findByNameContainingIgnoreCase(name);
        if (!existingMenuItem.isEmpty()) {
            throw new IllegalArgumentException("菜單名稱已存在！"); // 如果名稱已存在，拋出異常
        }
        // 創建新的 MenuItem 物件
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setSetContents(setContents);
        menuItem.setCategory(category);
        menuItem.setImage(imageUrl); // 設定圖片 URL（如果有的話）
        
        // 儲存菜單項目
        return menuItemRepository.save(menuItem);
    }
    
    
    
    public MenuItem updateMenuItem(String id, String name, String description, Double price, String setContents, String category, String imageUrl) throws IOException {
    // 檢查是否有其他菜單項目使用相同的名稱，但排除當前菜單項目
    List<MenuItem> existingMenuItem = menuItemRepository.findByNameContainingIgnoreCase(name);
    
    // 檢查是否有名稱相同且 id 不相同的菜單項目
    existingMenuItem = existingMenuItem.stream()
            .filter(item -> !item.getId().equals(id)) // 排除當前菜單項目
            .collect(Collectors.toList());

    if (!existingMenuItem.isEmpty()) {
        throw new IllegalArgumentException("菜單名稱已存在！"); // 如果名稱已存在，拋出異常
    }

    // 根據 ID 查找要更新的菜單項目
    MenuItem menuItem = findById(id);
    
    // 更新菜單項目的屬性
    menuItem.setName(name);
    menuItem.setDescription(description);
    menuItem.setPrice(price);
    menuItem.setSetContents(setContents);
    menuItem.setCategory(category);
    
    // 如果有新的圖片 URL，則更新圖片
    if (imageUrl != null && !imageUrl.isEmpty()) {
        menuItem.setImage(imageUrl);
    }

    // 儲存並返回更新後的菜單項目
    return menuItemRepository.save(menuItem);
}

    
    private MenuItem findById(String id) {
        // 使用 repository 查找對應的 menuItem
        return menuItemRepository.findById(id).orElseThrow();
    }

    public MenuItem getMenuById(String id) {
        return menuItemRepository.findById(id).orElseThrow();
    }
    // 根據多個菜單項目的 ID 查詢菜單項目
    public List<MenuItem> getMenuItemsByIds(List<String> menuItemIds) {
        // 使用 menuItemIds 查詢菜單項目列表
        List<MenuItem> menuItems = menuItemRepository.findAllById(menuItemIds);
        return menuItems;
    }
}
