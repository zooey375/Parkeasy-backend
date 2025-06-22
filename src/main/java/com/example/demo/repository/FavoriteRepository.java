package com.example.demo.repository;

import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByMember(Member member);
    Favorite findByMemberAndParkingLotId(Member member, Integer parkingLotId);
}
