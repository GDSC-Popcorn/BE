package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikeRepository extends JpaRepository<LikeEntity, Long> {
    //해당 사용자가 해당 팝업 찜 여부 확인
    boolean exitsByUserIdAndPopupId(Long userId, Long popupId);
}
