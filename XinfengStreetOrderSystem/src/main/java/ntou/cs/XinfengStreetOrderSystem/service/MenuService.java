package ntou.cs.XinfengStreetOrderSystem.service;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> searchMenuItems(String query) {
        return menuItemRepository.findByNameContainingIgnoreCase(query);
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    // 檢查是否存在菜單項目
    public boolean existsById(String id) {
        return menuItemRepository.existsById(id);  // 根據 ID 查找是否存在菜單項目
    }

    // 刪除菜單項目
    public void deleteMenuItem(String id) {
        menuItemRepository.deleteById(id);  // 根據 ID 刪除菜單項目
    }

}
