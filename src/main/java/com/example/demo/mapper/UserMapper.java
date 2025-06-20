package com.example.demo.mapper;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.Member;

public class UserMapper {

	// Entity -> DTO
	public static UserDto toDto(Member member) {
		if(member == null) return null;
		
		UserDto dto = new UserDto();
		dto.setId(member.getId());
		dto.setUsername(member.getUsername());
		dto.setRole(member.getRole());
		dto.setEmail(member.getEmail());
		dto.setConfirmEmail(member.isConfirmEmail());	// boolean 開頭是 is。
		return dto;
	}
	
	// DTO -> Entity(通常註冊時會用到)
	public static Member toEntity(UserDto dto) {
		if(dto == null) return null;
		
		Member member = new Member();
		member.setId(dto.getId());	// 若是新註冊的使用者，可以不用設 id。
		member.setUsername(dto.getUsername());
		member.setRole(dto.getRole());
		member.setEmail(dto.getEmail());
		member.setConfirmEmail(dto.isConfirmEmail());	// 預設 false
		return member;
	}
}
