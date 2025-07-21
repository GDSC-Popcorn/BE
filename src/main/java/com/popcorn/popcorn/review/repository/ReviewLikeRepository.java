package com.popcorn.popcorn.review.repository;

import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.review.entity.Review;
import com.popcorn.popcorn.review.entity.ReviewLike;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    //비관적 락(x-lock)
    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReviewLike> findByUserAndReview(UserEntity user, Review review);

    /*
    * 특정 사용자(:userId)가 좋아요를 누른 리뷰들 중, 주어진 리뷰 ID 목록(:reviewIds)에 포함된 리뷰의 ID를 반환
    * */
    @Query("select rl.review.id from ReviewLike rl " +
            "where rl.user.id = :userId and rl.review.id " +
            "IN :reviewIds")
    List<Long> findLikedReviewIdsByUserIdAndReviewIds(@Param("userId")Long userId, @Param("reviewIds") List<Long> reviewIds);

    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
}
