package com.popcorn.popcorn.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) //외래 키
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "popup_id",nullable = false)
    private PopupEntity popup;

    public LikeEntity(UserEntity user, PopupEntity popup) {
        this.user = user;
        this.popup = popup;
    }

}
