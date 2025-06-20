package com.example.demo.repository;

import com.example.demo.model.entity.Member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username); // 用來檢查是否重複帳號
}
