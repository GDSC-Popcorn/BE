package com.popcorn.popcorn.review.dto;

import com.popcorn.popcorn.domain.dto.PagedResponse;
import com.popcorn.popcorn.review.entity.Review;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ReviewRatingResponse(
        Float averageRating,
        Map<Integer, Integer> distribution,
        PagedResponse<ReviewResponse> reviews
) {
    public static ReviewRatingResponse from(float averageRating, Map<Integer, Integer> allRev, PagedResponse<ReviewResponse> rev) {
        return ReviewRatingResponse.builder()
                .averageRating(averageRating)
                .distribution(allRev)
                .reviews(rev)
                .build();
    }
}
