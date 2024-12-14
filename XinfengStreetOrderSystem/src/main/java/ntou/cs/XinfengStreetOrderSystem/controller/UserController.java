package ntou.cs.XinfengStreetOrderSystem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ntou.cs.XinfengStreetOrderSystem.entity.User;
import ntou.cs.XinfengStreetOrderSystem.service.MailService;
import ntou.cs.XinfengStreetOrderSystem.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        boolean isRegistered = userService.register(user);
        System.out.println("Register endpoint hit");
        if (isRegistered) {
            return ResponseEntity.ok("帳號註冊成功");
        } else {
            return ResponseEntity.badRequest().body("帳號已存在");
        }
    }

   // Login Endpoint
@PostMapping("/login")
public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest) {
    boolean isAuthenticated = userService.login(loginRequest); // 檢查帳號與密碼是否正確

    if (isAuthenticated) {
        // 根據用戶名或其他識別信息查詢用戶角色
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "用戶不存在"));
        }

        // 根據角色決定跳轉的頁面
        String redirectPage;
        if ("boss".equals(user.getRole())) {
            redirectPage = "menu-management"; // 老闆跳轉到菜單管理頁面
        } else if ("customer".equals(user.getRole())) {
            redirectPage = "menu-browse"; // 顧客跳轉到菜單瀏覽頁面
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "無效的用戶角色"));
        }

        // 返回成功訊息和跳轉路徑
        return ResponseEntity.ok(Map.of(
            "message", "登入成功",
            "role", user.getRole(),
            "redirect", redirectPage
        ));
    } else {
        return ResponseEntity.badRequest().body(Map.of("message", "帳號或密碼錯誤"));
    }
}

// 檢查帳號是否已存在
@GetMapping("/check-username")
public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
    boolean isTaken = userService.isUsernameTaken(username);
    return ResponseEntity.ok(isTaken);
}


    // 驗證碼
    @Autowired
    private MailService mailService; // 添加 MailService 的注入

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        String verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000); // 6 位數驗證碼
        mailService.sendVerificationEmail(email, verificationCode); // 通過實例調用
        return ResponseEntity.ok("驗證碼已寄出");
    }

    @RequestMapping("/api/mail")
    public class MailController {

    @Autowired
    private MailService mailService;

    private Map<String, String> verificationCodes = new HashMap<>();

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        String verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000); // 生成6位數隨機碼
        verificationCodes.put(email, verificationCode);
        mailService.sendVerificationEmail(email, verificationCode);
        return ResponseEntity.ok("驗證碼已寄出至：" + email);
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        String correctCode = verificationCodes.get(email);
        if (correctCode != null && correctCode.equals(code)) {
            verificationCodes.remove(email);
            return ResponseEntity.ok("驗證成功");
        }
        return ResponseEntity.badRequest().body("驗證失敗，請重新檢查驗證碼");
    }
}
}
