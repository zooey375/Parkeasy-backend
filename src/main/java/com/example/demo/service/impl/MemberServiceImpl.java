package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserExistException;
import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import com.example.demo.service.EmailService;
import com.example.demo.util.HashUtil;


import java.util.UUID;
import java.util.Optional;


@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private  MemberRepository memberRepository;
	
	// 寄信服務
	@Autowired
    private EmailService emailService; 

	// 新增會員帳號
	@Override
	public boolean addMember(String username, String password, String email) {
	    // 檢查帳號是否存在
		Optional<Member> memberOpt = memberRepository.findByUsername(username);
	    if (memberOpt.isPresent()) {
	        return false; // 帳號已存在
	    }
	    
	    // === 加密密碼 ===
	    String salt = HashUtil.generateSalt();                     // 產生鹽巴
	    String passwordHash = HashUtil.hashPassword(password, salt); // 加密密碼
	    String token = UUID.randomUUID().toString(); // 先產生 token

	    // 建立帳號資料
	    Member member = new Member();
	    member.setUsername(username);
	    member.setPassword(passwordHash);
	    member.setSalt(salt); // 加這一行才會把 salt 存入資料庫
	    member.setEmail(email);
	    member.setRole("USER");
	    member.setConfirmEmail(false); // 預設是未驗證
	    member.setVerificationToken(token); // 一次設好所有欄位
	    
	    memberRepository.save(member);	// 指呼叫一次 save()

	    emailService.sendVerificationEmail(email, token);

	    
	    return true; // 成功註冊

	}
}
		
		
	
	
	

