package ntou.cs.XinfengStreetOrderSystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import ntou.cs.XinfengStreetOrderSystem.entity.User;
import ntou.cs.XinfengStreetOrderSystem.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 註冊新用戶
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            boolean isRegistered = userService.register(user);
            if (isRegistered) {
                return ResponseEntity.ok("帳號註冊成功");
            } else {
                return ResponseEntity.badRequest().body("帳號已存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("伺服器內部錯誤");
        }
    }

    // 登入驗證
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest, HttpSession session) {
        try {
            boolean isAuthenticated = userService.login(loginRequest);
            if (isAuthenticated) {
                User user = userService.findByUsername(loginRequest.getUsername());
                if (user == null) {
                    return ResponseEntity.badRequest().body("用戶不存在");
                }

                // 將用戶 ID 存入 Session
                session.setAttribute("loggedInUser", user.getId());

                // 根據角色設定跳轉頁面
                String redirectPage = "boss".equals(user.getRole()) ? "/itemManage" : "/browseMenu";
                return ResponseEntity.ok(new UserResponse("登入成功", user.getRole(), redirectPage));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("伺服器內部錯誤");
        }
    }

    // 獲取當前登入用戶資訊
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Object loggedInUser = session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            return ResponseEntity.ok(Map.of("userId", loggedInUser));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入");
        }
    }

    // 登出
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            session.invalidate(); // 使 Session 失效
            return ResponseEntity.ok("已成功登出");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登出失敗");
        }
    }

    // 檢查帳號是否存在
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        try {
            boolean isTaken = userService.isUsernameTaken(username);
            return ResponseEntity.ok(isTaken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // 取得用戶收藏項目
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<User.FavoriteItem>> getFavoriteItems(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(userService.getFavoriteItemsList(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 加入收藏項目
    @PostMapping("/{userId}/favorites/add")
    public ResponseEntity<Void> addFavoriteItem(@PathVariable String userId,
            @RequestBody User.FavoriteItem favoriteItem) {
        try {
            userService.addFavoriteItem(userId, favoriteItem);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 刪除收藏項目
    @DeleteMapping("/{userId}/favorites/{menuItemId}/delete")
    public ResponseEntity<Void> removeFavoriteItem(@PathVariable String userId, @PathVariable String menuItemId) {
        try {
            userService.removeFavoriteItem(userId, menuItemId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 用於返回登入結果的內部類別
    private static class UserResponse {
        public String message;
        public String role;
        public String redirect;

        public UserResponse(String message, String role, String redirect) {
            this.message = message;
            this.role = role;
            this.redirect = redirect;
        }
    }
}
