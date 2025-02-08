package com.popcorn.popcorn.oauth.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OIDCPublickeyResponse {
    List<OIDCPublickeyDto> keys;
}
