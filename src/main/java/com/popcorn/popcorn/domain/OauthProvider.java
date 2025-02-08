package com.popcorn.popcorn.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OauthProvider {

    KAKAO("KAKAO");

    private String value;
}
