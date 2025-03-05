package com.popcorn.popcorn.oauth.apple.helper;


import com.popcorn.popcorn.domain.OauthProvider;
import com.popcorn.popcorn.domain.entity.OauthInfo;
import com.popcorn.popcorn.oauth.apple.AppleProperties;
import com.popcorn.popcorn.oauth.apple.client.AppleOauthClient;
import com.popcorn.popcorn.oauth.kakao.dto.OIDCDecodePayload;
import com.popcorn.popcorn.oauth.kakao.dto.OIDCPublickeyResponse;
import com.popcorn.popcorn.oauth.kakao.helper.OauthOIDCHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOauthHelper {

    private final AppleOauthClient appleOauthClient;
    private final OauthOIDCHelper oauthOIDCHelper;
    private final AppleProperties appleProperties;


    public OIDCDecodePayload getOIDCDecodePayload(String token){
        OIDCPublickeyResponse oidcPublickeyResponse = appleOauthClient.getAppleOIDCOpenKeys();
        return oauthOIDCHelper.getPayloadFromIdToken(
                token,
                appleProperties.getAppleBaseUrl(),
                appleProperties.getAppleClientId(),
                oidcPublickeyResponse
        );
    }

    public OauthInfo getOauthInfoByAppleIdToken(String idToken){
        OIDCDecodePayload oidcDecodePayload = getOIDCDecodePayload(idToken);
        return OauthInfo.builder()
                .provider(OauthProvider.APPLE)
                .oid(oidcDecodePayload.getSub())
                .build();
    }

}
