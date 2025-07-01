//實體類別

package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * 
Table name: ParkingLot
+---------------+-----------------+-----------------+------------+---------------+-------------+---------+
| ParkingLot_id | ParkingLot_name | ParkingLot_type |  friendly  |     price     | description | address |
+---------------+-----------------+-----------------+------------+---------------+-------------+---------+
|      1        |       中文       |     機車格、汽車格   | true/false |     0~100     |    評論      |   連結   |
+---------------+-----------------+-----------------+------------+---------------+-------------+---------+
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "friendly")
    private Boolean friendly;

    @Column(name = "price")
    private Integer price;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "map_url")
    private String mapUrl;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // 一個停車場可以被很多人收藏
    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // 避免前端請求時進入無限循環
    private List<Favorite> favorites;
}
