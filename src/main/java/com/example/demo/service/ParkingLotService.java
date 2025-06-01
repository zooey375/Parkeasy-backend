// 定義這隻程式的「位置」；它放在 service 資料夾下，是服務層(負責邏輯處理)
package com.example.demo.service;

import com.example.demo.model.entity.ParkingLot;	// 資料模型
import com.example.demo.repository.ParkingLotRepository;	//資料庫操作工具
import org.springframework.beans.factory.annotation.Autowired;	//讓 spring 自動幫我們建立 repository 的實例
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

    // 多條件查詢：名稱 / 種類 / 友善 / 價格
    //serch(...) 根據多個條件過濾(名稱、類型、友善、價格)，條件可有可無。
    public List<ParkingLot> search(String name, String type, Boolean friendly, String price) {  
        return repository.findAll().stream() // 先從資料庫拿出所有停車場資料，接著用 .stream()進行「一筆一筆過濾條件」。 
            .filter(p -> name == null || name.isEmpty() || 
                         (p.getName() != null && p.getName().contains(name))) 
            /* (上方)名稱模糊搜尋)；如果 name 是空的(使用者沒輸入)，就不過濾- GPT
            如果有輸入 name 就會篩選名稱中「包含這個關鍵字」的停車場。
            */
            .filter(p -> type == null || type.isEmpty() || 
                         (p.getType() != null && p.getType().equals(type)))
            
            .filter(p -> friendly == null || 
                         (p.getFriendly() != null && p.getFriendly().equals(friendly)))
            
            .filter(p -> price == null || price.isEmpty() || 
                         (p.getPrice() != null && p.getPrice().equals(price)))
            
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
}
