package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite")
@Data
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 關聯使用者：多對一
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // 防止 JSON 無限遞迴（Member -> Favorite -> Member -> ...）
    private Member member;

    // 關聯停車場：多對一
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    @JsonIgnoreProperties({"favorites"}) // 避免停車場內部的雙向關聯出現問題
    private ParkingLot parkingLot;

    // 時間欄位，可選
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
