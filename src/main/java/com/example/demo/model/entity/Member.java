package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	// 使用者id
	
	@Column(nullable = false, unique = true)
	private String username;	// 使用者帳號
	
	@Column(nullable = false)
	private String hashpassword;	// 使用者密碼
	private String salt;// 鹽巴
	private String role;		// 預設角色(admin/user)
	
	@Column(nullable = false)
	private String email;					// 信箱
	private boolean confirmEmail = false;	// 信箱驗證
	
}
