package com.popcorn.popcorn.oauth.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OIDCPublickeyDto {

    private String kid;
    private String alg;
    private String use;
    private String n;
    private String e;

}
