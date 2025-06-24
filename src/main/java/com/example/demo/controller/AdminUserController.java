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
        UserCert cert = (UserCert) session.getAttribute("user");
        if (cert == null) {
            System.out.println("⛔ 尚未登入");
            return ResponseEntity.status(401).body("請先登入");
        }
        if (!"ADMIN".equals(cert.getRole())) {
            return ResponseEntity.status(403).body("權限不足");
        }

        List<Member> allUsers = memberRepository.findAll();
        allUsers.forEach(user -> {
            user.setPassword(null);
            user.setSalt(null);
        });

        return ResponseEntity.ok(allUsers);
    }

    // 刪除會員（不允許刪除 admin）
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        Member user = memberRepository.findById(id).orElse(null);
        if (user == null) return "找不到會員";
        if ("ADMIN".equals(user.getRole())) return "禁止刪除管理員";

        memberRepository.deleteById(id);
        return "刪除成功";
    }

    // 新增會員
    @PostMapping("/users")
    public String createUser(@RequestBody Member newUser, HttpSession session) {
        UserCert cert = (UserCert) session.getAttribute("user");
        if (cert == null) return "請先登入";
        if (!"ADMIN".equals(cert.getRole())) return "權限不足";

        if (memberRepository.findByUsername(newUser.getUsername()) != null) {
            return "帳號已存在";
        }

        newUser.setConfirmEmail(true); // 預設已驗證（可自行調整）
        memberRepository.save(newUser);
        return "新增成功";
    }

    // 修改會員
    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody Member updateUser, HttpSession session) {
        UserCert cert = (UserCert) session.getAttribute("user");
        if (cert == null) return "請先登入";
        if (!"ADMIN".equals(cert.getRole())) return "權限不足";

        Member user = memberRepository.findById(id).orElse(null);
        if (user == null) return "找不到會員";

        user.setUsername(updateUser.getUsername());
        user.setEmail(updateUser.getEmail());
        user.setRole(updateUser.getRole());

        // 如果有輸入密碼就更新
        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
            user.setPassword(updateUser.getPassword());
        }

        memberRepository.save(user);
        return "修改成功";
    }
}
