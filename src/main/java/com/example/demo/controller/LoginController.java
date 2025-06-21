package com.example.demo.controller;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Member;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CertService;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.PasswordInvalidException;


@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CertService certService;


     // 登入：接收 username、password，驗證後寫入 Session
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserCert>> login(@RequestBody Member member, HttpSession session) {
        String username = member.getUsername();
        String password = member.getPassword();

        try {
            UserCert cert = certService.login(username, password);
            session.setAttribute("loginUser", cert); // 存入 Session
            return ResponseEntity.ok(ApiResponse.success("登入成功", cert));
        } catch (UserNotFoundException | PasswordInvalidException e) {
        	// 帳號或密碼錯誤
            return ResponseEntity.status(401).body(ApiResponse.error(401, e.getMessage()));
        } catch (RuntimeException e) {
        	// 信箱未驗證或其他自定義錯誤
            return ResponseEntity.status(403).body(ApiResponse.error(403, e.getMessage()));
        } catch (Exception e) {
        	// 不明錯誤
            return ResponseEntity.status(500).body(ApiResponse.error(500, "伺服器錯誤"));
        }
    }

    
     // 註冊：接收 username、password、email，新增帳號
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody Member member) {
        boolean result = memberService.addMember(member.getUsername(), member.getPassword(), member.getEmail());

        if (!result) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "帳號已存在"));
        }

        return ResponseEntity.ok(ApiResponse.success("註冊成功", null));
    }


     // 取得目前登入資訊

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserCert>> getLoginUser(HttpSession session) {
        UserCert cert = (UserCert) session.getAttribute("loginUser");
        if (cert == null) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "尚未登入"));
        }
        return ResponseEntity.ok(ApiResponse.success("目前登入資訊", cert));
    }

   
     // 登出：移除 Session
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.success("已登出", null));
    }
}

