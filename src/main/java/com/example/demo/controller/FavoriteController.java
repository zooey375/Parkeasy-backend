package com.example.demo.controller;

import com.example.demo.model.entity.Favorite;
import com.example.demo.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // 新增收藏
    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer parkingLotId
    ) {
        favoriteService.addFavorite(userId, parkingLotId); // ← 改對變數名稱
        return ResponseEntity.ok("Added to favorites");
    }


    // 取消收藏
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer parkingLotId
    ) {
        favoriteService.removeFavorite(userId, parkingLotId); // ← 改對變數名稱
        return ResponseEntity.ok("Removed from favorites");
    }

    // 查詢使用者的收藏清單
    @GetMapping("/{userId}")
    public ResponseEntity<List<Favorite>> getFavorites(
            @PathVariable Integer userId
    ) {
        List<Favorite> favorites = favoriteService.getFavoritesByUser(userId);
        return ResponseEntity.ok(favorites);
    }
}
