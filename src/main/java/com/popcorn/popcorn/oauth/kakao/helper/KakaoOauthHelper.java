package com.popcorn.popcorn.oauth.kakao.helper;

import com.popcorn.popcorn.domain.OauthProvider;
import com.popcorn.popcorn.domain.entity.OauthInfo;
import com.popcorn.popcorn.oauth.kakao.KakaoProperties;
import com.popcorn.popcorn.oauth.kakao.client.KakaoOauthClient;
import com.popcorn.popcorn.oauth.kakao.dto.OIDCDecodePayload;
import com.popcorn.popcorn.oauth.kakao.dto.OIDCPublickeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KakaoOauthHelper {

    private final KakaoOauthClient kakaoOauthClient;
    private final OauthOIDCHelper oauthOIDCHelper;
    private final KakaoProperties kakaoProperties;

    /*
    * 두번째 세번쨰 인자로 카카오 baseurl, app id를 받아야하는데 프론트랑 일치시켜줘야하는지?
    * */
    public OIDCDecodePayload getOIDCDecodePayload(String token){
        OIDCPublickeyResponse oidcPublickeyResponse = kakaoOauthClient.getOIDCPublickeys();
        return oauthOIDCHelper.getPayloadFromIdToken(
                token,
                kakaoProperties.getKakaoBaseUrl(),
                kakaoProperties.getKakaoAppKey(),
                oidcPublickeyResponse
        );
    }



    public OauthInfo getOauthInfoByKakaoIdToken(String idToken){
        OIDCDecodePayload oidcDecodePayload = getOIDCDecodePayload(idToken);
        return OauthInfo.builder()
                .provider(OauthProvider.KAKAO)
                .oid(oidcDecodePayload.getSub())
                .build();
    }
}
