package com.example.demo.repository;

import com.example.demo.model.entity.Member; 
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByUsername(String username);
	

}
