package com.example.demo.controller;

import com.example.demo.model.dto.FavoriteDTO;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Favorite;
import com.example.demo.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // 取得收藏
    @GetMapping("/favorites")
    public List<FavoriteDTO> getFavorites(HttpSession session) {
        UserCert user = (UserCert) session.getAttribute("user"); 
        if (user == null) {
            throw new RuntimeException("請先登入");
        }
        return favoriteService.getFavoritesByUserId(user.getId()); 
    }

    // 新增收藏
    @PostMapping("/{parkingLotId}")
    public void addFavorite(@PathVariable Integer parkingLotId, HttpSession session) {
        UserCert user = (UserCert) session.getAttribute("user"); 
        if (user == null) {
            throw new RuntimeException("請先登入");
        }
        favoriteService.addFavorite(user.getId(), parkingLotId);
    }

    // 移除收藏
    @DeleteMapping("/{parkingLotId}")
    public void removeFavorite(@PathVariable Integer parkingLotId, HttpSession session) {
        UserCert user = (UserCert) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("請先登入");
        }
        favoriteService.removeFavorite(user.getId(), parkingLotId);
    }
}
