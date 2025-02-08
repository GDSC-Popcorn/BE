package com.popcorn.popcorn.oauth.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class OIDCDecodePayload {
    /** issuer ex https://kauth.kakao.com */
    private String iss;

    /** client id */
    private String aud;

    /** oauth provider account unique id */
    private String sub;

    //email 을 받아오는지?
    private String email;
}
