package com.popcorn.popcorn.review.service;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.common.exception.PopUpNotFoundExceptioin;
import com.popcorn.popcorn.common.exception.ReviewNotFoundExceptioin;
import com.popcorn.popcorn.common.exception.ReviewNotMatchException;
import com.popcorn.popcorn.common.exception.UserNotFoundException;
import com.popcorn.popcorn.domain.dto.PagedResponse;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.repository.PopupRepository;
import com.popcorn.popcorn.repository.UserRepository;
import com.popcorn.popcorn.review.dto.ReviewRatingResponse;
import com.popcorn.popcorn.review.dto.ReviewRequest;
import com.popcorn.popcorn.review.dto.ReviewResponse;
import com.popcorn.popcorn.review.entity.Review;
import com.popcorn.popcorn.review.entity.ReviewImage;
import com.popcorn.popcorn.review.entity.ReviewLike;
import com.popcorn.popcorn.review.repository.ReviewLikeRepository;
import com.popcorn.popcorn.review.repository.ReviewRepository;
import com.popcorn.popcorn.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final UserRepository userRepository;
    private final PopupRepository popupRepository;
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public ReviewResponse createReview(@Valid ReviewRequest request, List<MultipartFile> imgs, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        PopupEntity popup = popupRepository.findById(request.popupId()).orElseThrow(() -> PopUpNotFoundExceptioin.EXCEPTION);


        Review review = request.toEntity(user, popup);
        reviewRepository.save(review);
        if(imgs != null) {
            for(MultipartFile file: imgs) {
                // 파일이 비어있으면 건너뜀 (0KB 방지)
                if (file.isEmpty()) continue;
                String key = s3Service.reviewImageUpload(file, review.getId());
                review.addReviewImage(new ReviewImage(key));
            }
            reviewRepository.save(review);
        }

        return ReviewResponse.from(review, s3Service, false);
    }

    @Transactional
    public ReviewResponse modifyReview(Long reviewId, @Valid ReviewRequest request, List<MultipartFile> imgs, Long userId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundExceptioin.EXCEPTION);
        if(review.getUser().getId() != userId) {
            throw ReviewNotMatchException.EXCEPTION;
        }
        for(ReviewImage img : review.getReviewImages()) {
            s3Service.deleteImage(img.getImageKey());
        }
        review.clearReviewImages();


        if(imgs != null) {
            for(MultipartFile file : imgs) {
                // 파일이 비어있으면 건너뜀 (0KB 방지)
                if (file.isEmpty()) continue;

                String key = s3Service.reviewImageUpload(file, review.getId());
                review.addReviewImage(new ReviewImage(key));
            }
        }

        review.update(request.content(), request.rating());
        reviewRepository.save(review);

        boolean liked = reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId);

        return ReviewResponse.from(review ,s3Service, liked);
    }

    @Transactional
    public void deleteById(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundExceptioin.EXCEPTION);
        if(review.getUser().getId() != userId) {
            throw ReviewNotMatchException.EXCEPTION;
        }
        for(ReviewImage img : review.getReviewImages()) {
            s3Service.deleteImage(img.getImageKey());
        }

        reviewRepository.delete(review);
    }

    public ReviewRatingResponse getReviewsByPopup(Long popupId, Long userId, Pageable pageable) {
        PopupEntity popup = popupRepository.findById(popupId).
                orElseThrow(() -> PopUpNotFoundExceptioin.EXCEPTION);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Page<Review> reviews =  reviewRepository.findByPopupWithUserAndImages(popup, pageable);

        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
        List<Long> likedReviewIds = reviewLikeRepository.findLikedReviewIdsByUserIdAndReviewIds(userId, reviewIds);
        Set<Long> likedReviewIdSet = new HashSet<>(likedReviewIds);

        Page<ReviewResponse> res = reviews.map(r -> ReviewResponse.from(r, s3Service, likedReviewIdSet.contains(r.getId())));
        PagedResponse<ReviewResponse> reviewPagedResponse = PagedResponse.from(res);

        //전체 리뷰 평점 계산
        List<Review> allRev = reviewRepository.findAllByPopup(popup);

        float averageRating = (float) allRev.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        averageRating = Math.round(averageRating*10) /10.0f + 1;

        Map<Integer, Integer> distribution = new HashMap<>();
        for(int i=0;i<=4;i++) {
            distribution.put(i, 0);
        }

        allRev.forEach(r -> {
            int rating = r.getRating();
            distribution.put(rating, distribution.getOrDefault(rating,0) + 1);
        });

        return ReviewRatingResponse.from(averageRating, distribution, reviewPagedResponse);
    }

    @Transactional
    public boolean toggleLike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundExceptioin.EXCEPTION);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Optional<ReviewLike> existing = reviewLikeRepository.findByUserAndReview(user, review);

        if (existing.isPresent()) {
            reviewLikeRepository.delete(existing.get());
            review.decreaseLikeCount();  // likeCount -= 1
            return false;
        } else {
            reviewLikeRepository.save(new ReviewLike(user, review));
            review.increaseLikeCount();  // likeCount += 1
            return true;
        }

    }
}
