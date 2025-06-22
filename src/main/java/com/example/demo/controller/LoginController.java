package com.example.demo.controller;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CertService;
import com.example.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.PasswordInvalidException;

// 導向前端頁面
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

// 建立 Spring Security 的認證資訊
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CertService certService;
    
    @Autowired
    private MemberRepository memberRepository;


     // 登入：接收 username、password，驗證後寫入 Session
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserCert>> login(@RequestBody Member member, HttpSession session) {
        String username = member.getUsername();
        String password = member.getPassword();

        try {
        	// 驗證帳號密碼
            UserCert cert = certService.login(username, password);
            
            // 設定 Spring Security 的使用者認證與角色
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            	cert.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + cert.getRole())) // 角色前綴需加 ROLE_
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 存入 Session
            session.setAttribute("user", cert); 
            return ResponseEntity.ok(ApiResponse.success("登入成功", cert));
        } catch (UserNotFoundException | PasswordInvalidException e) {
        	// 帳號或密碼錯誤
            return ResponseEntity.status(401)
            		.body(ApiResponse.error(401, e.getMessage()));
        } catch (RuntimeException e) {
        	// 信箱未驗證或其他自定義錯誤
            return ResponseEntity.status(403)
            		.body(ApiResponse.error(403, e.getMessage()));
        } catch (Exception e) {
        	// 不明錯誤
            return ResponseEntity.status(500)
            		.body(ApiResponse.error(500, "伺服器錯誤"));
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
}

