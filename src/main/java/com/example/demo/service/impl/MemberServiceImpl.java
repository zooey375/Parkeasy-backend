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
	public void addMember(String username, String password, String Email) {
		Optional<Member> memberOpt=memberRepository.findByUsername(username);
		if(memberOpt.isPresent()) {
			throw new UserExistException("帳號已被註冊");
		}
		if(memberOpt.isEmpty()) {
			Member member=new Member();
					String salt;
					salt=HashUtil.generateSalt();
					String passwordHash=HashUtil.hashPassword(password, salt);
					
					member.setUsername(username);
					member.setHashpassword(passwordHash);
					member.setEmail(Email);
					member.setRole("USER");
					memberRepository.save(member);
		}
			
		}
		
		
	}
	
	

