//實體類別

package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
Table name: ParkingLot
+---------------+-----------------+-----------------+------------+---------------+-------------+---------+
| ParkingLot_id | ParkingLot_name | ParkingLot_type |  friendly  |     price     | description | address |
+---------------+-----------------+-----------------+------------+---------------+-------------+---------+
|      1        |       中文      |  機車格、汽車格  | true/false | 免費/小時/每日 |    評論     |  連結   |
+---------------+-----------------+-----------------+------------+---------------+-------------+---------+
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 設定主建宇自動編號
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type") // 可為"機車格"、"汽車格"
    private String type;

    @Column(name = "friendly")
    private Boolean friendly; // 是否為友善（true/false）

    @Column(name = "price") // 可為"免費"、"每小時"、"每日"
    private String price;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "map_url")
    private String mapUrl; // 放 Google Map 連結用
    
    @Column(name = "latitude") //Y-緯度座標
    private Double latitude;

    @Column(name = "longitude") //X-經度座標
    private Double longitude;
}
