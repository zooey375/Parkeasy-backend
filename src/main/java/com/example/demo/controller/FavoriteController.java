package com.example.demo.controller;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Favorite;
import com.example.demo.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // 取得收藏
    @GetMapping
    public List<Favorite> getMyFavorites(HttpSession session) {
        UserCert loginUser = (UserCert) session.getAttribute("user");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "請先登入");
        }
        return favoriteService.getByUserId(loginUser.getId());
    }

    // 加入收藏
    @PostMapping("/{parkingLotId}")
    public void addFavorite(@PathVariable Integer parkingLotId, HttpSession session) {
        UserCert loginUser = (UserCert) session.getAttribute("user");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "請先登入");
        }
        favoriteService.addFavorite(loginUser.getId(), parkingLotId);
    }

    // 移除收藏
    @DeleteMapping("/{parkingLotId}")
    public void removeFavorite(@PathVariable Integer parkingLotId, HttpSession session) {
        UserCert loginUser = (UserCert) session.getAttribute("user");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "請先登入");
        }
        favoriteService.removeFavorite(loginUser.getId(), parkingLotId);
    }
}
