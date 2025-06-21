// 創建 admin 帳密

package com.example.demo.config;

import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.HashUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DataInitializer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        if (memberRepository.findByUsername("admin").isEmpty()) {
            String salt = HashUtil.generateSalt();
            String password = HashUtil.hashPassword("admin123", salt);

            Member admin = new Member();
            admin.setUsername("admin");
            admin.setPassword(password);
            admin.setSalt(salt);
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");
            admin.setConfirmEmail(true); // 已驗證

            memberRepository.save(admin);
            System.out.println("預設 admin 帳號已建立：帳號 admin / 密碼 admin123");
        }
    }
}
