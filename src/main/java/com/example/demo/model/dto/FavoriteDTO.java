package com.example.demo.model.dto;

import com.example.demo.model.entity.ParkingLot;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FavoriteDTO {
    private Integer id;
    private String name;
    private String type;
    private Boolean friendly;
    private Integer price;
    private String description;
    private String address;
    private String mapUrl;
    private Double latitude;
    private Double longitude;

 // 建構子：從 ParkingLot 實體轉成 DTO
    public FavoriteDTO(ParkingLot lot) {
        this.id = lot.getId();
        this.name = lot.getName();
        this.type = lot.getType();
        this.friendly = lot.getFriendly();
        this.price = lot.getPrice();
        this.description = lot.getDescription();
        this.address = lot.getAddress();
        this.mapUrl = lot.getMapUrl();
        this.latitude = lot.getLatitude();
        this.longitude = lot.getLongitude();
    }
}
