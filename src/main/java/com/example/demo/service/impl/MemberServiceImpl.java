package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserExistException;
import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import com.example.demo.util.HashUtil;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private  MemberRepository memberRepository;

	@Override
	public boolean addMember(String username, String password, String email) {
	    Optional<Member> memberOpt = memberRepository.findByUsername(username);

	    if (memberOpt.isPresent()) {
	        return false; // 帳號已存在
	    }

	    // 新增會員
	    String salt = HashUtil.generateSalt();                     // 產生鹽巴
	    String passwordHash = HashUtil.hashPassword(password, salt); // 加密密碼

	    Member member = new Member();
	    member.setUsername(username);
	    member.setPassword(passwordHash);
	    member.setSalt(salt); // 加這一行才會把 salt 存入資料庫
	    member.setEmail(email);
	    member.setRole("USER");
	    member.setConfirmEmail(false); // 初始為未驗證
	    memberRepository.save(member);
	
	    return true; // 成功註冊

	}
}
		
		
	
	
	

