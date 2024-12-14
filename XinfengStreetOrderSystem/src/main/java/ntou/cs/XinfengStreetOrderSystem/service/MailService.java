package ntou.cs.XinfengStreetOrderSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("新豐街早餐店 <你的Gmail地址@gmail.com>"); // 指定寄件者名稱
        message.setTo(to);
        message.setSubject("新豐街早餐店 - 驗證碼");
        message.setText("您好，\n\n您的驗證碼是：" + verificationCode + "\n\n請於10分鐘內完成驗證。");
        mailSender.send(message);
    }
}
