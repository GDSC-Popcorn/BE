package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.entity.PopupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopupRepository extends JpaRepository <PopupEntity, Long>{
    //종료일 오름차순으로 팝업 목록 정렬
    List<PopupEntity> findAllByOrderByEnded_atAsc();
    //팝업 ID로 팝업 조회
    PopupEntity findByPopupId(Long popupId);
}
