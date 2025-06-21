package com.example.demo.service.impl;

import com.example.demo.exception.PasswordInvalidException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;

import com.example.demo.service.CertService;
import com.example.demo.util.HashUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CertServiceImpl implements CertService{
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Override
	public UserCert login(String username, String password) {
		
		// 1. 查詢使用者
		Member member=memberRepository.findByUsername(username)
				.orElseThrow(()->new UserNotFoundException("找不到使用者"));
		
		// 用查到的 salt 加密使用者輸入的密碼
	    String hashedInputPassword = HashUtil.hashPassword(password, member.getSalt());
		
		// 2. 驗證密碼(使用加密比對)
		 //if (!password.equals(member.getHashpassword())) { // 暫時不加密版本
	    if (!hashedInputPassword.equals(member.getPassword())) {
		        throw new PasswordInvalidException("密碼錯誤");
		 }
		
		 
		 // 3. 檢查信箱是否驗證通過
		 if(!member.isConfirmEmail()) {
			 throw new RuntimeException("請先完成信箱驗證");
		 }
		 
		// 4. 登入成功，回應 UserCert
		return new UserCert(member.getId(), member.getRole());
	}

}
