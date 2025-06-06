package com.popcorn.popcorn.review.dto;

import com.popcorn.popcorn.review.entity.Review;
import com.popcorn.popcorn.review.entity.ReviewImage;
import com.popcorn.popcorn.service.S3Service;

import java.time.LocalDateTime;
import java.util.List;


public record ReviewResponse(
        Long id,
        String contents,
        Double rating,
        List<String> imageUrls,
        LocalDateTime createdAt,
        int likeCount,
        String nickname
) {
    public static ReviewResponse from(Review review, S3Service s3Service) {
        return new ReviewResponse(
                review.getId(),
                review.getContents(),
                review.getRating(),
                review.getReviewImages().stream()
                                .map(ReviewImage::getImageKey)
                                        .map(s3Service::getReviewImageUrl)
                                                .toList(),
                review.getCreatedAt(),
                review.getLikeCount(),
                review.getUser().getNickname()
        );
    }

}
