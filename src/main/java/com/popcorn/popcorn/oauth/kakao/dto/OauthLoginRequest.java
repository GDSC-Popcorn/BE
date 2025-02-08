package com.popcorn.popcorn.oauth.kakao.dto;

import lombok.Data;

@Data
public class OauthLoginRequest {

    private String idToken;
    private String provider;
}
