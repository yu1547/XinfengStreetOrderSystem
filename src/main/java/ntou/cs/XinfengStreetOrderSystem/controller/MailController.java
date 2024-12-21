package ntou.cs.XinfengStreetOrderSystem.controller;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ntou.cs.XinfengStreetOrderSystem.service.MailService;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    // 儲存驗證碼（email -> code）
    private Map<String, String> verificationCodes = new HashMap<>();

    // 發送驗證碼 API
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        // 生成 6 位數隨機碼
        String verificationCode = String.valueOf((int) (Math.random() * 900000) + 100000);

        // 將驗證碼儲存至 Map
        verificationCodes.put(email, verificationCode);

        // 發送郵件
        mailService.sendVerificationEmail(email, verificationCode);

        return ResponseEntity.ok("驗證碼已寄出至：" + email);
    }

    // 驗證驗證碼 API
    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("verificationCode");

        // 檢查驗證碼是否正確
        String correctCode = verificationCodes.get(email);
        if (correctCode != null && correctCode.equals(code)) {
            verificationCodes.remove(email); // 驗證成功後移除驗證碼
            return ResponseEntity.ok("驗證成功");
        }
        return ResponseEntity.badRequest().body("驗證失敗，請重新檢查驗證碼");
    }

    // 發送訂單通知 API
    @PostMapping("/sendOrderNotification")
    public ResponseEntity<String> sendOrderNotification(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String orderDetails = payload.get("orderDetails");

        // 設定郵件內容
        String messageContent = "您好，\n\n您的訂單詳細資料如下：\n" + orderDetails + "\n\n謝謝您的支持！!   !";

        // 發送訂單通知郵件
        mailService.sendOrderNotificationEmail(email, messageContent);

        return ResponseEntity.ok("訂單通知已寄出至：" + email);
    }
}

