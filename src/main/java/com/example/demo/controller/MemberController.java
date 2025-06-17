package com.example.demo.controller;

// --- 匯入需要用到的類別 ---
import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController // 這是一個 REST 控制器，會回傳 JSON 格式資料
@RequestMapping("/api/members") // 所有路徑都以這開頭，例如 /api/members/register
@CrossOrigin(origins = "http://localhost:5173") // 前端網址允許連線（React 通常是這個 port）
public class MemberController {

    @Autowired // 自動注入 Repository（等同 new 一個）
    private MemberRepository memberRepository;

    // 1. 查詢所有會員（GET）
    @GetMapping("")
    public List<Member> getAllMembers() {
        return memberRepository.findAll(); // 從資料庫撈出所有 members
    }

    // 2. 註冊新會員（POST）
    @PostMapping("/register")
    public String register(@RequestBody Member member) {
        // 檢查帳號是否重複
        if (memberRepository.findByUsername(member.getUsername()) != null) {
            return "帳號已存在";
        }

        // 儲存新會員
        memberRepository.save(member);
        return "註冊成功";
    }

    // 3. 登入會員（POST）
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Member member) {
        Member found = memberRepository.findByUsername(member.getUsername());

        Map<String, Object> response = new HashMap<>();

        if (found != null && found.getPassword().equals(member.getPassword())) {
            response.put("message", "登入成功");
            response.put("username", found.getUsername());
            response.put("isAdmin", found.isAdmin()); // 回傳管理員身分
            response.put("userId", found.getId());	// 前端需要這個 userId 才知道是誰。
        } else {
            response.put("message", "帳號或密碼錯誤");
        }

        return response;
    }
}

