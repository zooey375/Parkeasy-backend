// 專門處理「使用者發出的請求」
package com.example.demo.controller;

import com.example.demo.model.entity.ParkingLot;	// 資料模型
import com.example.demo.service.ParkingLotService;	// 處理資料模型(查詢用 service)
import org.springframework.beans.factory.annotation.Autowired;	// 把 service 倉庫建好不用手動 new
import org.springframework.web.bind.annotation.*;

import java.util.List;

 // 這三項是設定標籤
@RestController // API 控制器，使用者可以透過網址來呼叫
@RequestMapping("/parking-lots")
@CrossOrigin(origins = "*") // 讓前端可以 fetch
public class ParkingLotController {

    @Autowired
    private ParkingLotService service;

    // 查詢所有停車場
    @GetMapping
    public List<ParkingLot> getAllParkingLots() {
        return service.findAll(); // 呼叫 service 去找資料
    }

    // 根據條件查詢（名稱 / 類型 / 友善 / 價格）
    @GetMapping("/search")
    public List<ParkingLot> search(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Boolean friendly,
        @RequestParam(required = false) String price
    ) {
        return service.search(name, type, friendly, price);
    }
    
 // 新增一筆停車場
    @PostMapping
    public ParkingLot add(@RequestBody ParkingLot parkingLot) {
        return service.save(parkingLot);	// save 會幫我新增或更新一筆資料(JPA提供)
    }

    // 刪除一筆停車場（根據 id）
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);	// deleteById 會幫我刪除一筆資料(JPA提供)
    }
    
    
}
