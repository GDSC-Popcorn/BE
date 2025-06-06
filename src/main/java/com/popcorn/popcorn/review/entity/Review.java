package com.popcorn.popcorn.review.entity;

import com.popcorn.popcorn.domain.common.BaseEntity;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private double rating;//평점

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewImage> reviewImages = new ArrayList<>();

    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_id")
    private PopupEntity popup;

    public void addReviewImage(ReviewImage img) {
        reviewImages.add(img);
        img.setReview(this);
    }

    public void clearReviewImages() {
        for(ReviewImage img : reviewImages) {
            img.setReview(null);
        }
        reviewImages.clear();
    }

    public void update(String content, double rating) {
        this.contents = content;
        this.rating = rating;
    }
}
