package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopupRepository extends JpaRepository <PopupEntity,  Long>{
    //종료일 오름차순으로 팝업 목록 정렬
    Page<PopupEntity> findAllByOrderByEndedAtAsc(Pageable pageable);
    //사용자의 관심 카테고리에 해당하는 팝업 조회
    List<PopupEntity> findByInterestIn(List<InterestType> interest, Pageable pageable);
    //특정 관심 카테고리 전체보기
    Page<PopupEntity> findByInterest(InterestType interest, Pageable pageable);
}
