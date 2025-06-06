package com.popcorn.popcorn.review.controller;

import com.popcorn.popcorn.common.api.ApiResponse;
import com.popcorn.popcorn.domain.dto.CustomUserDetails;
import com.popcorn.popcorn.domain.dto.PagedResponse;
import com.popcorn.popcorn.review.dto.ReviewRequest;
import com.popcorn.popcorn.review.dto.ReviewResponse;
import com.popcorn.popcorn.review.entity.Review;
import com.popcorn.popcorn.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    //한 팝업에 대한 모든 리뷰 조회
    @GetMapping("/popups/{popupId}")
    public ResponseEntity<ApiResponse<PagedResponse<ReviewResponse>>> getAllReview(
            @PathVariable Long popupId,
            @PageableDefault(size =20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(reviewService.getReviesByPopup(popupId, pageable))
        );
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @RequestPart(value = "request") @Valid ReviewRequest request,
            @RequestPart(value = "images", required = false)List<MultipartFile> imgs,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        ;
        return ResponseEntity.ok().body(ApiResponse.ok(reviewService.createReview(request, imgs, userDetails.getUserId())));
    }


    @PostMapping("/modify/{reviewId}")
    public ResponseEntity<?> modifyReview(
            @PathVariable Long reviewId,
            @RequestPart("request") @Valid ReviewRequest request,
            @RequestPart(value = "images", required = false)List<MultipartFile> imgs,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseEntity.ok().body(ApiResponse.ok(reviewService.modifyReview(reviewId, request, imgs, userDetails.getUserId())));
    }


    @DeleteMapping("/delete/{reviewId}")
    public void deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        reviewService.deleteById(reviewId, userDetails.getUserId());

    }
}
