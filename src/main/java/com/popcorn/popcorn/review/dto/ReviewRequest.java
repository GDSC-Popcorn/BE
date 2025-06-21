package com.popcorn.popcorn.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.review.entity.Review;
import jakarta.validation.constraints.*;

public record ReviewRequest(
        @NotBlank(message = "리뷰 내용은 비어있을 수 없습니다.")
        @Size(min = 10, message = "리뷰 내용은 최소 10자 이상이어야 합니다.")
        String content,

        @NotNull(message = "평점은 필수입니다.")
        @DecimalMin(value = "0.0", inclusive = true, message = "평점은 0점 이상이어야 합니다.")
        @DecimalMax(value = "5.0", inclusive = true, message = "평점은 5점 이하여야 합니다.")
        int rating,

        @NotNull
        Long popupId
) {

        public Review toEntity(UserEntity user, PopupEntity popup) {
                return Review.builder()
                        .contents(content)
                        .rating(rating)
                        .user(user)
                        .popup(popup)
                        .build();
        }
}
