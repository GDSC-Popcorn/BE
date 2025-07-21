package com.popcorn.popcorn.review.repository;

import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.review.entity.Review;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndPopup(UserEntity user, PopupEntity popup);

    Page<Review> findByPopup(PopupEntity popup, Pageable pageable);

    @Query("select r from Review r " +
            "join fetch r.user " +
            "left join fetch r.reviewImages " +
            "where r.popup = :popup"
    )
    Page<Review> findByPopupWithUserAndImages(@Param("popup") PopupEntity popup, Pageable pageable);

    List<Review> findAllByPopup(PopupEntity popup);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Review r where r.id=:reviewId")
    Optional<Review> findByIdWithLock(@Param("reviewId")Long reviewId);
}
