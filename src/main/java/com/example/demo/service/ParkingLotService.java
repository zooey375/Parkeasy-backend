// 定義這隻程式的「位置」；它放在 service 資料夾下，是服務層(負責邏輯處理)
package com.example.demo.service;

import com.example.demo.model.entity.ParkingLot;	// 資料模型
import com.example.demo.repository.ParkingLotRepository;	//資料庫操作工具
import org.springframework.beans.factory.annotation.Autowired;	//讓 spring 自動幫我們建立 repository 的實例
import org.springframework.boot.ExitCodeEvent;
import org.springframework.stereotype.Service;	// java 的資料過濾工具

import java.util.List;
import java.util.stream.Collectors;

@Service //代表這是一個服務層元件
public class ParkingLotService {

    @Autowired // 會自動幫忙建立 repository 倉庫物件 -> 可以用 repository.findAll() 直接存取資料庫。
    private ParkingLotRepository repository;

    // getAll()查詢全部停車場資料
    public List<ParkingLot> getAll() {
        return repository.findAll();
    }

 // 多條件查詢：名稱 / 種類 / 友善 / 價格範圍調整成下拉選單*2
    /**
     * 🔍 多條件搜尋方法（升級版）：
     * @param name      停車場名稱（可部分比對）
     * @param type      機車格或汽車格
     * @param friendly  是否為友善
     * @param minprice  最低價格（可為 null）
     * @param maxprice  最高價格（可為 null）
     * @return          符合條件的停車場清單
     */
    
    public List<ParkingLot> search(String name, String type, Boolean friendly, Integer minprice, Integer maxprice) {
        return repository.findAll().stream()
        	// 模糊比對名稱(如果有輸入)
            .filter(p -> name == null || name.isEmpty() || 
                         (p.getName() != null && p.getName().contains(name)))
            // 比對類型
            .filter(p -> type == null || type.isEmpty() || 
                         (p.getType() != null && p.getType().equals(type)))
            // 比對友善與否
            .filter(p -> friendly == null || 
                         (p.getFriendly() != null && p.getFriendly().equals(friendly)))
            // 比對最低價格(如果有輸入)
            .filter(p -> minprice == null || 
            			 (p.getPrice() != null && p.getPrice() >= minprice))
         // 比對最高價格（如果有輸入）
            .filter(p -> maxprice == null ||
                        (p.getPrice() != null && p.getPrice() <= maxprice))
                       
            .collect(Collectors.toList());
    }
    
    // 新增一筆停車場
    public ParkingLot save(ParkingLot parkingLot) {
        return repository.save(parkingLot);
    }

    // 刪除一筆停車場
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    
    // 修改資料方法
    public ParkingLot update(Integer id, ParkingLot updatedLot) {
    	ParkingLot existing = repository.findById(id)
    			.orElseThrow(() -> new RuntimeException("找不到該筆資料"));
    	
    	existing.setName(updatedLot.getName());
    	existing.setType(updatedLot.getType());
    	existing.setFriendly(updatedLot.getFriendly());
    	existing.setPrice(updatedLot.getPrice());
    	existing.setAddress(updatedLot.getAddress());
    	existing.setDescription(updatedLot.getDescription());
    	existing.setLatitude(updatedLot.getLatitude());
    	existing.setLongitude(updatedLot.getLongitude());
    	existing.setMapUrl(updatedLot.getMapUrl());
    	
    	return repository.save(existing);
    }
    
}
