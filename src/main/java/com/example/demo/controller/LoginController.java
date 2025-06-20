package com.example.demo.controller;

import com.example.demo.exception.UserExistException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Member;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CertService;
import com.example.demo.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private CertService certService;
    @Autowired
    private MemberService memberService;
   

    
     // 1.登入 API：接收帳號密碼，回傳登入憑證 
    @PostMapping("/login")
    public ResponseEntity<UserCert> login(@RequestParam String username,
                                          @RequestParam String password) {
        UserCert cert = certService.login(username, password);
        return ResponseEntity.ok(cert);
    }
    
    // 2.註冊 API :接收帳號、密碼、Email，存入資料庫
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam String email) {
    	try {
    		memberService.addMember(username, password, email);
		} catch (UserExistException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
		}
    	
		return ResponseEntity.ok(ApiResponse.success("member創建成功", null));
        

        
    }

    	// 3.信箱驗證 API（模擬驗證連結）
	    @GetMapping("/verify")
	    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
			return null;
	        
	    }
    
}

