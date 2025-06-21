package com.example.demo.controller;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    private MemberRepository memberRepository;

    // 取得所有會員（不含密碼）
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        UserCert cert = (UserCert) session.getAttribute("loginUser");

        // 沒登入
        if (cert == null) {
            return ResponseEntity.status(401).body("請先登入");
        }

        // 不是 ADMIN
        if (!"ADMIN".equals(cert.getRole())) {
            return ResponseEntity.status(403).body("權限不足");
        }

        // 可以執行
        List<Member> allUsers = memberRepository.findAll();
        allUsers.forEach(user -> {
            user.setPassword(null);
            user.setSalt(null);
        });

        return ResponseEntity.ok(allUsers);
    }

    // 刪除會員（不允許刪除 admin 自己）
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        Member user = memberRepository.findById(id).orElse(null);
        if (user == null) return "找不到會員";
        if ("ADMIN".equals(user.getRole())) return "禁止刪除管理員";
        
        memberRepository.deleteById(id);
        return "刪除成功";
    }
}
