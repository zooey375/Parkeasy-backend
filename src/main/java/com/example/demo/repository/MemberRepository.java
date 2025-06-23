package com.example.demo.repository;

import com.example.demo.model.entity.Member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username); // 用來檢查是否重複帳號
    Optional<Member> findByVerificationToken(String token); // 信箱
    Optional<Member> findByEmail(String email);	// 驗證信箱
    Optional<Member> findByResetToken(String token);	// 使用者重設密碼


}
