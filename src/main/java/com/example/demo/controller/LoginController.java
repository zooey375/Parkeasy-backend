package com.example.demo.controller;

import com.example.demo.model.dto.ForgotPasswordRequest;
import com.example.demo.model.dto.LoginRequest;
import com.example.demo.model.dto.ResetPasswordRequest;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.EmailService;
import com.example.demo.service.MemberService;
import com.example.demo.util.HashUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 導向前端頁面
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

// 建立 Spring Security 的認證資訊
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;
    

    @Autowired
    private EmailService emailService;


     // 登入：接收 username、password，驗證後寫入 Session
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request,
            HttpSession session,
            HttpServletRequest httpRequest) {
        Optional<Member> memberOpt = memberRepository.findByUsername(request.getUsername());
        if (memberOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "帳號不存在"));
        }

        Member member = memberOpt.get();
        String hashedPassword = HashUtil.hashPassword(request.getPassword(), member.getSalt());

        if (!member.getPassword().equals(hashedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "密碼錯誤"));
        }
        
        // 建立 Spring Security 專用的 UserPrincipal
        UserPrincipal principal = new UserPrincipal(member);
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
     // 強制寫入 Spring Security context 到 session
        httpRequest.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


        // 把登入成功的使用者資訊存入 session
        //session.setAttribute("user", member);
        UserCert cert = new UserCert(member.getId(), member.getUsername(), member.getEmail(), member.getRole());
        session.setAttribute("user", cert);
        

        
        // 回傳登入成功訊息與會員資料（可自行決定要不要全部傳）
        return ResponseEntity.ok(Map.of(
            "message", "登入成功",
            "data", Map.of(
                "id", member.getId(),
                "username", member.getUsername(),
                "email", member.getEmail(),
                "role", member.getRole()
            )
        ));
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
    	UserCert cert = (UserCert) session.getAttribute("user"); 
    	
        if (cert == null) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "尚未登入"));
        }
        return ResponseEntity.ok(ApiResponse.success("登入成功", cert));
    }

   
     // 登出：移除 Session 與 Spring Security 登入資訊
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext(); // 清除 Spring Security 的登入狀態
        return ResponseEntity.ok(ApiResponse.success("已登出", null));
    }
        
        
     // Email 驗證
     @GetMapping("/verify")
     public ResponseEntity<Void> verifyEmail(@RequestParam("token") String token) {
    	    Optional<Member> memberOpt = memberRepository.findByVerificationToken(token);

    	    if (memberOpt.isEmpty()) {
    	        // 驗證失敗時導向註冊頁或錯誤提示
    	        return ResponseEntity.status(HttpStatus.FOUND)
    	        		// HttpHeaders.LOCATION -> 導去前端網址
    	                .header(HttpHeaders.LOCATION, "http://localhost:5173/register?verify=fail")
    	                .build();
    	    }

    	    Member member = memberOpt.get();
    	    member.setConfirmEmail(true); // 標記為已驗證
    	    member.setVerificationToken(null); // 清除 token 避免重複點擊
    	    memberRepository.save(member);

    	    // 驗證成功 → 導向前端 EmailConfirmSuccess 頁面
    	    return ResponseEntity.status(HttpStatus.FOUND)
    	            .header(HttpHeaders.LOCATION, "http://localhost:5173/EmailConfirmSuccess")
    	            .build();
    	}
     
     // 忘記密碼
     @PostMapping("/forgot-password")
     public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
         Optional<Member> memberOpt = memberRepository.findByEmail(request.getEmail());

         if (memberOpt.isEmpty()) {
             return ResponseEntity.badRequest().body("查無此 Email");
         }

         Member member = memberOpt.get();

         // 產生 Token 與過期時間
         String token = UUID.randomUUID().toString();
         member.setResetToken(token);
         member.setTokenExpiry(LocalDateTime.now().plusMinutes(30));
         memberRepository.save(member);

         // 發送信件
         String resetLink = "http://localhost:5173/reset-password?token=" + token;
         emailService.sendResetPasswordEmail(member.getEmail(), resetLink);
         
         
         System.out.println("🔑 測試用 token = " + token); // 印出 token 到 console

         return ResponseEntity.ok("已寄出重設密碼信件");
         
     }
      
     // 使用者修改密碼
     @PostMapping("/reset-password")
     public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
         Optional<Member> memberOpt = memberRepository.findByResetToken(request.getToken());

         if (memberOpt.isEmpty()) {
             return ResponseEntity.badRequest().body("Token 無效，請重新申請重設密碼");
         }

         Member member = memberOpt.get();

         // 驗證 token 是否過期
         if (member.getTokenExpiry() == null || member.getTokenExpiry().isBefore(LocalDateTime.now())) {
             return ResponseEntity.badRequest().body("Token 已過期，請重新申請重設密碼");
         }

         // 設定新密碼（記得 hash 處理）
         String salt = member.getSalt(); // 使用原本的 salt
         String hashed = HashUtil.hashPassword(request.getNewPassword(), salt);
         member.setPassword(hashed);

         // 清除 token 與過期時間
         member.setResetToken(null);
         member.setTokenExpiry(null);

         memberRepository.save(member);
         return ResponseEntity.ok("密碼重設成功");
     }


}

