package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, name = "hashpassword")
    private String password;

    private String salt;
    private String role;

    @Column(nullable = false)
    private String email;

    private boolean confirmEmail = false;


    // 一位會員可以收藏多筆停車場
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore 
    private List<Favorite> favorites;
    
    // 建立忘記密碼
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;
    
    @Column(name = "verification_token")
    private String verificationToken;
}
