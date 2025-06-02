package com.example.demo.repository;

import com.example.demo.model.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    // 根據使用者 ID 查出他所有收藏的紀錄
    List<Favorite> findByUserId(Integer userId);

    // 查詢某使用者是否已收藏某停車場（用來避免重複加入）
    Optional<Favorite> findByUserIdAndParkingLotId(Integer userId, Integer parkingLotId);

    // 刪除某使用者對某停車場的收藏紀錄
    void deleteByUserIdAndParkingLotId(Integer userId, Integer parkingLotId);
}
