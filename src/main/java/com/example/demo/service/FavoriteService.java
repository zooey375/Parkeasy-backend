package com.example.demo.service;

import com.example.demo.model.dto.FavoriteDTO;
import com.example.demo.model.entity.Favorite;
import com.example.demo.model.entity.Member;
import com.example.demo.model.entity.ParkingLot;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ParkingLotRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    // 改用 userId 查詢
    public List<FavoriteDTO> getFavoritesByUserId(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(userId);
        
        return favorites.stream()
                .filter(fav -> fav.getParkingLot() != null) // 避免 null
                .map(fav -> new FavoriteDTO(fav.getParkingLot()))
                .collect(Collectors.toList());
    }

    public void addFavorite(Long userId, Integer parkingLotId) {
    	boolean exists = favoriteRepository
    		.findByMemberIdAndParkingLotId(userId, parkingLotId)
    		.isPresent();

    		if (exists) {
    			return; // 已存在就不要再加
    		}
    		
        Member member = memberRepository.findById(userId).orElseThrow();
        ParkingLot lot = parkingLotRepository.findById(parkingLotId).orElseThrow();

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setParkingLot(lot);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Long userId, Integer parkingLotId) {
    	  Favorite favorite = favoriteRepository
    		        .findByMemberIdAndParkingLotId(userId, parkingLotId)
    		        .orElseThrow(() -> new RuntimeException("找不到收藏記錄"));

    		    favoriteRepository.delete(favorite);
    		}
}

