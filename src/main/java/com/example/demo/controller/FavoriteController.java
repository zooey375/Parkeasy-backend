package com.example.demo.controller;

import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.ParkingLot;
import com.example.demo.service.FavoriteService;
import com.example.demo.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;
    
    //製作完整資料合併
    @Autowired
    private ParkingLotService parkingLotService;

    // 新增收藏
    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer parkingLotId
    ) {
        favoriteService.addFavorite(userId, parkingLotId); // ← 改對變數名稱
        return ResponseEntity.ok("已加入收藏");
    }


    // 取消收藏
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer parkingLotId
    ) {
        favoriteService.removeFavorite(userId, parkingLotId); // ← 改對變數名稱
        return ResponseEntity.ok("已取消收藏");
    }

    // 查詢使用者的收藏清單，但回傳「完整停車場資訊」
    @GetMapping("/{userId}")
    public ResponseEntity<List<ParkingLot>> getFavorites(@PathVariable Integer userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUser(userId); // ← 補這行

        // 透過收藏的 parkingLotId 去找對應的停車場資訊
    	List<ParkingLot> parkingLots = favorites.stream()
                .map(fav -> parkingLotService.getAll().stream()
                        .filter(p -> p.getId().equals(fav.getParkingLotId()))
                        .findFirst()
                        .orElse(null))
                .filter(p -> p != null) // 過濾掉查不到的
                .collect(Collectors.toList());

        return ResponseEntity.ok(parkingLots);
    }
}
