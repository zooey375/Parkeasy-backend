package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = true)
    private String verificationToken;

    // ✅ 一位會員可以收藏多筆停車場
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 和 Favorite 的 @JsonBackReference 配對，避免無限遞迴
    private List<Favorite> favorites;
}
