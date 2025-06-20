package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 無參數建構子
@AllArgsConstructor // 有參數建構子
public class UserDto {
    private Long id;
    private String username;
    private String role;
    private String email;
    private boolean confirmEmail;
}

