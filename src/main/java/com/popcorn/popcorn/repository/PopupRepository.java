package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopupRepository extends JpaRepository <PopupEntity, Long>{
    //종료일 오름차순으로 팝업 목록 정렬
    Page<PopupEntity> findAllByOrderByEndedAtAsc(Pageable pageable);
    //사용자의 관심사와 일치하는 팝업 검색
    @Query("SELECT p FROM PopupEntity p JOIN p.categories c WHERE c IN :categories ORDER BY p.startedAt ASC")
    List<PopupEntity> findByCategories(@Param("categories") List<InterestType> categories);
    //카테고리로 팝업 조회
    List<PopupEntity> findByCategories(InterestType categories);

}
