package com.popcorn.popcorn.review.dto;

import com.popcorn.popcorn.review.entity.Review;
import com.popcorn.popcorn.review.entity.ReviewImage;
import com.popcorn.popcorn.service.S3Service;

import java.time.LocalDateTime;
import java.util.List;


public record ReviewResponse(
        Long id,
        String contents,
        int rating,
        List<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        int likeCount,
        String nickname,
        Long profileId,
        boolean liked
) {
    public static ReviewResponse from(Review review, S3Service s3Service, boolean liked) {
        return new ReviewResponse(
                review.getId(),
                review.getContents(),
                review.getRating(),
                review.getReviewImages().stream()
                                .map(ReviewImage::getImageKey)
                                        .map(s3Service::getReviewImageUrl)
                                                .toList(),
                review.getCreatedAt(),
                review.getModifiedAt(),
                review.getLikeCount(),
                review.getUser().getNickname(),
                review.getUser().getProfileId(),
                liked

        );
    }

}
