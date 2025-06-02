package com.example.demo.service;

import com.example.demo.model.entity.Favorite;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    // 新增收藏（如果還沒收藏過）
    public void addFavorite(Integer userId, Integer parkingLotId) {
        boolean notExists = favoriteRepository
                .findByUserIdAndParkingLotId(userId, parkingLotId)
                .isEmpty();

        if (notExists) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setParkingLotId(parkingLotId);
            favorite.setCreatedAt(LocalDateTime.now());
            favoriteRepository.save(favorite);
        }
    }

    // 移除收藏
    public void removeFavorite(Integer userId, Integer parkingLotId) {
        favoriteRepository.deleteByUserIdAndParkingLotId(userId, parkingLotId);
    }


    // 查詢某使用者所有收藏
    public List<Favorite> getFavoritesByUser(Integer userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
