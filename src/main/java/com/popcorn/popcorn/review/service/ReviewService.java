package com.popcorn.popcorn.review.service;

import com.popcorn.popcorn.common.exception.PopUpNotFoundExceptioin;
import com.popcorn.popcorn.common.exception.ReviewNotFoundExceptioin;
import com.popcorn.popcorn.common.exception.ReviewNotMatchExceptioin;
import com.popcorn.popcorn.common.exception.UserNotFoundException;
import com.popcorn.popcorn.domain.dto.PagedResponse;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.repository.PopupRepository;
import com.popcorn.popcorn.repository.UserRepository;
import com.popcorn.popcorn.review.dto.ReviewRequest;
import com.popcorn.popcorn.review.dto.ReviewResponse;
import com.popcorn.popcorn.review.entity.Review;
import com.popcorn.popcorn.review.entity.ReviewImage;
import com.popcorn.popcorn.review.repository.ReviewRepository;
import com.popcorn.popcorn.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final UserRepository userRepository;
    private final PopupRepository popupRepository;
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponse createReview(@Valid ReviewRequest request, List<MultipartFile> imgs, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        PopupEntity popup = popupRepository.findById(request.popupId()).orElseThrow(() -> PopUpNotFoundExceptioin.EXCEPTION);


        Review review = request.toEntity(user, popup);
        reviewRepository.save(review);
        if(imgs != null) {
            for(MultipartFile file: imgs) {
                String key = s3Service.reviewImageUpload(file, review.getId());
                review.addReviewImage(new ReviewImage(key));
            }
            reviewRepository.save(review);
        }

        return ReviewResponse.from(review, s3Service);
    }

    @Transactional
    public ReviewResponse modifyReview(Long reviewId, @Valid ReviewRequest request, List<MultipartFile> imgs, Long userId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundExceptioin.EXCEPTION);

        for(ReviewImage img : review.getReviewImages()) {
            s3Service.deleteImage(img.getImageKey());
        }
        review.clearReviewImages();


        if(imgs != null) {
            for(MultipartFile file : imgs) {
                String key = s3Service.reviewImageUpload(file, review.getId());
                review.addReviewImage(new ReviewImage(key));
            }
        }

        review.update(request.content(), request.rating());
        reviewRepository.save(review);
        return ReviewResponse.from(review ,s3Service);
    }

    public void deleteById(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundExceptioin.EXCEPTION);
        if(review.getUser().getId() != userId) {
            throw ReviewNotMatchExceptioin.EXCEPTION;
        }
        for(ReviewImage img : review.getReviewImages()) {
            s3Service.deleteImage(img.getImageKey());
        }

        reviewRepository.delete(review);
    }

    public PagedResponse<ReviewResponse> getReviesByPopup(Long popupId, Pageable pageable) {
        PopupEntity popup = popupRepository.findById(popupId).
                orElseThrow(() -> PopUpNotFoundExceptioin.EXCEPTION);

        Page<ReviewResponse> rev =  reviewRepository.findByPopup(popup, pageable)
                .map(review -> ReviewResponse.from(review, s3Service));
        return PagedResponse.from(rev);
    }
}
