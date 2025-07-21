package com.popcorn.popcorn.review.service;

import com.popcorn.popcorn.domain.Role;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.repository.PopupRepository;
import com.popcorn.popcorn.repository.UserRepository;
import com.popcorn.popcorn.review.entity.Review;
import com.popcorn.popcorn.review.repository.ReviewLikeRepository;
import com.popcorn.popcorn.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ReviewServiceTest2 {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PopupRepository popupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    private Review review;
    private List<UserEntity> users;
    private UserEntity user1;
    private PopupEntity popup;

    @BeforeEach
    void setup() {
        popup = popupRepository.save(new PopupEntity());
        user1 = userRepository.save(UserEntity.builder().username("user1129784679182")
                .email("user1232534@test.com")
                .password("test1234")
                .role(Role.ROLE_USER).build());

        review = reviewRepository.save(new Review(null, "즐겁네요~~~~~~", 4, new ArrayList<>() , 0, user1, popup));

        users = IntStream.range(1, 100)
                .mapToObj(i -> UserEntity.builder()
                        .username("user" + i)
                        .email("user" + i + "@test.com")
                        .password("pass" + i)
                        .role(Role.ROLE_USER)
                        .build())
                .map(userRepository::save)
                .collect(Collectors.toList());

    }


    @Test
    void toggleLike() throws InterruptedException {
// Given
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Long reviewId = review.getId();

        // When
        for (int i = 0; i < threadCount; i++) {
            final Long userId = users.get(i).getId();
            executor.execute(() -> {
                try {
                    reviewService.toggleLike(reviewId, userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // Then
        Review updatedReview = reviewRepository.findById(reviewId).orElseThrow();
        long likeCount = updatedReview.getLikeCount();

        assertEquals(50, likeCount);
    }
}
