// 專門處理「使用者發出的請求」
package com.example.demo.controller;

import com.example.demo.model.entity.ParkingLot;	// 資料模型
import com.example.demo.service.ParkingLotService;	// 處理資料模型(查詢用 service)
import org.springframework.beans.factory.annotation.Autowired;	// 把 service 倉庫建好不用手動 new
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // API 控制器，使用者可以透過網址來呼叫
@RequestMapping("/api/parkinglots")
@CrossOrigin(origins = "*") // 讓前端可以 fetch(允許跨域請求)
public class ParkingLotController {

    @Autowired
    private ParkingLotService service;

    // 查詢所有停車場
    @GetMapping
    public List<ParkingLot> getAllParkingLots() {
        return service.getAll(); // 這裡呼叫的是 service 裡的 getAll()
    }
    
    // 根據 ID 查詢（提供給收藏功能）
    @GetMapping("/get/{id}")
    public ParkingLot getById(@PathVariable Integer id) {
        return service.getById(id);
    }
    // 根據條件查詢（名稱 / 類型 / 友善 / 價格）
    @GetMapping("/search")
    public List<ParkingLot> search(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Boolean friendly,
        @RequestParam(required = false) Integer minprice,
        @RequestParam(required = false) Integer maxprice
    ) {
    	
        return service.search(name, type, friendly, minprice, maxprice);
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
    
    
    // 修改資料功能
    @PutMapping("/{id}") // 對應 PUT 請求
    public ParkingLot update(@PathVariable Integer id, @RequestBody ParkingLot updatedLot) {
        return service.update(id, updatedLot);
    }

    

}
