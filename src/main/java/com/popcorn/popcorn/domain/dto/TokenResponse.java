package com.popcorn.popcorn.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponse {


    private final String accessToken;
    private final String refreshToken;
    private String accessExpiredAt;
    private String refreshExpiredAt;

}
