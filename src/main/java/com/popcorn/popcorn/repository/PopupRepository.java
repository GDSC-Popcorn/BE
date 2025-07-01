package com.popcorn.popcorn.repository;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.dto.HomeDto;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    //Dto Projection으로 성능 개선
    @Query(
        value = "select new com.popcorn.popcorn.domain.dto.HomeDto(" +
                "p.id, p.title, CONCAT(:baseUrl, '/', p.id, '/01.jpg'), " +
                "p.startedAt, p.endedAt, p.location, p.interest) " +
                "from PopupEntity p order by p.endedAt asc",
            countQuery = "select count(p) from PopupEntity p"

    )
    Page<HomeDto> findAllProjected(@Param("baseUrl") String imageBaseUrl, Pageable pageable);

    @Query("SELECT new com.popcorn.popcorn.domain.dto.HomeDto(" +
            "p.id, p.title, CONCAT(:baseUrl, '/', p.id, '/01.jpg'), " +
            "p.startedAt, p.endedAt, p.location, p.interest) " +
            "FROM PopupEntity p " +
            "ORDER BY FUNCTION('RAND')")
    List<HomeDto> findRandomPopuups(@Param("baseUrl") String baseUrl, Pageable pageable);

}
