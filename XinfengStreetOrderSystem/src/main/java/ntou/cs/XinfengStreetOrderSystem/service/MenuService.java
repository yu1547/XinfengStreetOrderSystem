package ntou.cs.XinfengStreetOrderSystem.service;

import ntou.cs.XinfengStreetOrderSystem.entity.MenuItem;
import ntou.cs.XinfengStreetOrderSystem.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public MenuItem addMenuItem(String name, String description, Double price, String setContents, String category,
            MultipartFile image) throws IOException {
        String imageName = fileStorageService.storeFile(image);
        String imageUrl = "/uploads/" + imageName;
        // 儲存圖片並取得檔案名稱

        // 新增餐點物件
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setSetContents(setContents);
        menuItem.setCategory(category);
        menuItem.setImage(imageUrl);

        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(String id, String name, String description, Double price, String setContents,
            String category, MultipartFile image) throws IOException {
        MenuItem menuItem = findById(id);
        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setCategory(category);
        menuItem.setSetContents(setContents);

        if (image != null && !image.isEmpty()) {

            String imageName = fileStorageService.storeFile(image);
            String imageUrl = "/uploads/" + imageName;
            menuItem.setImage(imageUrl);
        }

        return menuItemRepository.save(menuItem);

    }

    private MenuItem findById(String id) {
        // 使用 repository 查找對應的 menuItem
        return menuItemRepository.findById(id).orElseThrow();
    }

    public MenuItem getMenuById(String id) {
        return menuItemRepository.findById(id).orElseThrow();
    }
}
