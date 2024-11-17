package com.popcorn.popcorn.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String refresh;

    private LocalDateTime expiration;

}
