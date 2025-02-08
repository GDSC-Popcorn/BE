package com.popcorn.popcorn.service;


import com.popcorn.popcorn.domain.entity.RefreshEntity;
import com.popcorn.popcorn.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RefreshService {

    private final RefreshRepository refreshRepository;

    public void addRefreshEntity(String username, String refresh, long plusHour){
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(plusHour);

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .username(username)
                .refresh(refresh)
                .expiration(expirationDate)
                .build();

        refreshRepository.save(refreshEntity);
    }
}
