package com.example.demo.repository;

import com.example.demo.model.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
   
	// 根據會員 ID 查詢該會員的所有收藏
	List<Favorite> findByMemberId(Long memberId);
	
	// 根據會員 ID 與停車場 ID 刪除指定收藏
    //void deleteByMemberIdAndParkingLotId(Long memberId, Integer parkingLotId);
    Favorite findByMemberIdAndParkingLotId(Long memberId, Integer parkingLotId);


}
