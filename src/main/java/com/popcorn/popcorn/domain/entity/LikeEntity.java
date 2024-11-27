package com.popcorn.popcorn.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "likes")
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) //외래 키
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "popupId",nullable = false)
    private PopupEntity popup;

    public LikeEntity(UserEntity user, PopupEntity popup) {
        this.user = user;
        this.popup = popup;
    }

}
