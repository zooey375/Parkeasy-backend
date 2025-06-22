package com.example.demo.service;

import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.Member;
import com.example.demo.model.entity.ParkingLot;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private MemberRepository memberRepository;

    // 查詢指定使用者的所有收藏
    public List<Favorite> getByUserId(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));
        return favoriteRepository.findByMember(member);
    }

    // 新增收藏
    public void addFavorite(Long userId, Integer parkingLotId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        ParkingLot lot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new RuntimeException("找不到指定的停車場"));

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setParkingLot(lot);
        favoriteRepository.save(favorite);
    }

    // 取消收藏
    public void removeFavorite(Long userId, Integer parkingLotId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        Favorite fav = favoriteRepository.findByMemberAndParkingLotId(member, parkingLotId);
        if (fav != null) {
            favoriteRepository.delete(fav);
        }
    }
}

