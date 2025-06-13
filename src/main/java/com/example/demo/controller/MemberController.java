package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.Member;
import com.example.demo.repository.MemberRepository;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:5173") 
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;
	
	// 註冊功能 API
	@PostMapping("/register")
	public String register(@RequestBody Member member) {
		if(memberRepository.findByUsername(member.getUsername()) != null) {
			return "帳號已存在";
		}
		memberRepository.save(member);
		return "註冊成功";
	}
	
	// 登入功能 API
	@PostMapping("/login")
	public String login(@RequestBody Member member) {
		Member found = memberRepository.findByUsername(member.getUsername());
		
		if (found != null && found.getPassword().equals(member.getPassword())) {
			return "登入成功";
		} else {
			return "帳號或密碼錯誤";
		}
	}
}
