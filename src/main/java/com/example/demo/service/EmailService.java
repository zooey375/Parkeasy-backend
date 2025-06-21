package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String token) {
        String subject = "請驗證您的帳號";
        String verifyUrl = "http://localhost:8086/api/auth/verify?token=" + token;
        String content = "<p>親愛的使用者，您好：</p>"
                + "<p>請點擊以下連結完成帳號驗證：</p>"
                + "<p><a href=\"" + verifyUrl + "\">點我驗證帳號</a></p>"
                + "<br><p>如果您沒有註冊此帳號，請忽略此信。</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true); // 第二個參數設為 true 代表內容是 HTML

            mailSender.send(message);

            System.out.println("✅ 驗證信已寄出至: " + email);
        } catch (MessagingException e) {
            System.err.println("❌ 發送驗證信失敗：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
