package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.entity.LikeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLikeRepository extends JpaRepository<LikeEntity, Long> {
    //해당 사용자가 해당 팝업 찜 여부 확인
    boolean existsByUserIdAndPopupId(Long userId, Long popupId);
    //특정 사용자가 찜한 모든 LikeEntity 조회
    List<LikeEntity> findAllByUserId(Long userId, Pageable pageable);
    //가장 최근에 찜한 순서로 10개만 조희
    List<LikeEntity> findTop10ByUserIdOrderByIdDesc(Long userId);

}
