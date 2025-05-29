package com.example.demo.service;

import com.example.demo.model.entity.ParkingLot;
import com.example.demo.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository repository;

    // getAll()查詢全部停車場資料
    public List<ParkingLot> getAll() {
        return repository.findAll();
    }

    // 多條件查詢：名稱 / 種類 / 友善 / 價格
    //serch(...) 根據多個條件過濾(名稱、類型、友善、價格)，條件可有可無。
    public List<ParkingLot> search(String name, String type, Boolean friendly, String price) {  
        return repository.findAll().stream()
            .filter(p -> name == null || name.isEmpty() || 
                         (p.getName() != null && p.getName().contains(name)))
            .filter(p -> type == null || type.isEmpty() || 
                         (p.getType() != null && p.getType().equals(type)))
            .filter(p -> friendly == null || 
                         (p.getFriendly() != null && p.getFriendly().equals(friendly)))
            .filter(p -> price == null || price.isEmpty() || 
                         (p.getPrice() != null && p.getPrice().equals(price)))
            .collect(Collectors.toList());
    }
}
