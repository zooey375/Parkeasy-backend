// 用來與資料庫互動（CRUD 查詢）
package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//Spring JPA
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {
	// 預設會實現 CRUD
	// 自定義查詢
	// 自動生成 SQL
    // 1.模糊查詢名稱（前端輸入關鍵字）
    List<ParkingLot> findByNameContaining(String name);

    // 2.格位種類查詢（"機車格", "汽車格"）
    List<ParkingLot> findByType(String type);

    // 3.友善停車場查詢（true/false）
    List<ParkingLot> findByFriendly(Boolean friendly);

    // 4.收費方式查詢（調整成價錢區間 0~200 元）
    List<ParkingLot> findByPriceBetween(Integer min, Integer max);
}