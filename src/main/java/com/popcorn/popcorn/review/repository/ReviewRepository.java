package com.popcorn.popcorn.review.repository;

import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndPopup(UserEntity user, PopupEntity popup);

    Page<Review> findByPopup(PopupEntity popup, Pageable pageable);
}
