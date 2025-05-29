package com.example.demo.controller;

import com.example.demo.model.entity.ParkingLot;
import com.example.demo.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-lots")
@CrossOrigin(origins = "*") // 讓前端可以 fetch
public class ParkingLotController {

    @Autowired
    private ParkingLotService service;

    @GetMapping
    public List<ParkingLot> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    public List<ParkingLot> search(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Boolean friendly,
        @RequestParam(required = false) String price
    ) {
        return service.search(name, type, friendly, price);
    }
}
