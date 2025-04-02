package com.popcorn.popcorn.oauth.kakao.helper;

import com.popcorn.popcorn.oauth.kakao.dto.OIDCDecodePayload;
import com.popcorn.popcorn.oauth.kakao.dto.OIDCPublickeyDto;
import com.popcorn.popcorn.oauth.kakao.dto.OIDCPublickeyResponse;
import com.popcorn.popcorn.oauth.kakao.provider.JwtOIDCProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthOIDCHelper {

    private final JwtOIDCProvider jwtOIDCProvider;

    // OauthOIDC는 스펙이기때문에 OauthOIDCHelper 하나로 카카오,구글 다 대응 가능하다.
    // KakaoOauthHelper 등에서 아래 소스들을 사용한다.
    // kid를 토큰에서 가져온다.
    private String getKidFromUnsignedToken(String token, String iss, String aud){
        return jwtOIDCProvider.getKidFromUnsignedTokenHeader(token, iss, aud);
    }

    /**
     * ID Token의 payload를 추출하는 메서드
     * OAuth 2.0 spec에 따라 ID Token의 유효성 검사 수행
     * @param token : idToken
     * @param iss : ID Token을 발급한 provider의 URL
     * @param aud : ID Token이 발급된 앱의 앱 키
     * //@param nonce : 인증 서버 로그인 요청 시 전달한 임의의 문자열
     * @param oidcPublickeyResponse : 공개키 목록
     * @return OIDCDecodePayload : ID Token의 payload
     */
    public OIDCDecodePayload getPayloadFromIdToken(
            String token, String iss, String aud, OIDCPublickeyResponse oidcPublickeyResponse
    ){
        String kid = getKidFromUnsignedToken(token, iss, aud);

        OIDCPublickeyDto key = oidcPublickeyResponse.getKeys().stream()
                .filter(k -> k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching key found"));

        return jwtOIDCProvider.getOIDCTokenBody(token, key.getN(), key.getE(), iss, aud);
    }


}
